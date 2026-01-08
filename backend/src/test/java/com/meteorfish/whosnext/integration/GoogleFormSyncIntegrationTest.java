package com.meteorfish.whosnext.integration;

import com.meteorfish.whosnext.infrastructure.external.google.GoogleFormRow;
import com.meteorfish.whosnext.infrastructure.external.google.GoogleFormScheduler;
import com.meteorfish.whosnext.infrastructure.external.google.GoogleSheetsService;
import com.meteorfish.whosnext.infrastructure.external.google.SyncMetadataRepository;
import com.meteorfish.whosnext.infrastructure.external.mcp.McpClient;
import com.meteorfish.whosnext.infrastructure.persistence.config.SyncMetadataEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingRepository;
import com.meteorfish.whosnext.infrastructure.persistence.review.StagingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
public class GoogleFormSyncIntegrationTest {

    @Autowired private GoogleFormScheduler googleFormScheduler;

    @Autowired private ReviewStagingRepository stagingRepository;

    @Autowired private SyncMetadataRepository metadataRepository;

    @MockBean
    private GoogleSheetsService googleSheetsService;

    @MockBean
    private McpClient mcpClient;

    @Test
    @DisplayName("구글 시트로부터 새로운 데이터를 성공적으로 동기화하고 인덱스를 업데이트한다")
    void syncGoogleFormDataTest() {
        // given
        String syncKey = "GOOGLE_SHEETS_REVIEW";
        int initialRowIndex = 1;
        metadataRepository.save(new SyncMetadataEntity(syncKey, initialRowIndex));

        List<GoogleFormRow> mockRows = List.of(
                new GoogleFormRow("user1@test.com", "Samsung", "Title 1", "Content 1", "Tip 1", "5", "6 months", "Java", "Backend"),
                new GoogleFormRow("user2@test.com", "Naver", "Title 2", "Content 2", "Tip 2", "4", "3 months", "Python", "AI")
        );

        given(googleSheetsService.getNewResponses(initialRowIndex)).willReturn(mockRows);

        // when
        googleFormScheduler.fetchGoogleFormData();

        // then
        List<ReviewStagingEntity> savedStaging = stagingRepository.findAll();
        assertThat(savedStaging).hasSize(2);
        assertThat(savedStaging.get(0).getRawMemberEmail()).isEqualTo("user1@test.com");
        assertThat(savedStaging.get(1).getRawCompanyName()).isEqualTo("Naver");
        assertThat(savedStaging.get(0).getStatus()).isEqualTo(StagingStatus.PENDING);

        SyncMetadataEntity updatedMetadata = metadataRepository.findById(syncKey).orElseThrow();
        assertThat(updatedMetadata.getLastRowIndex()).isEqualTo(3);
    }

    @Test
    @DisplayName("새로운 데이터가 없을 경우 인덱스가 변하지 않는다")
    void syncEmptyDataTest() {
        // given
        String syncKey = "GOOGLE_SHEETS_REVIEW";
        metadataRepository.save(new SyncMetadataEntity(syncKey, 5));
        given(googleSheetsService.getNewResponses(5)).willReturn(List.of());

        // when
        googleFormScheduler.fetchGoogleFormData();

        // then
        SyncMetadataEntity metadata = metadataRepository.findById(syncKey).orElseThrow();
        assertThat(metadata.getLastRowIndex()).isEqualTo(5);
    }

    @Test
    @DisplayName("PENDING 상태의 리뷰들을 MCP를 통해 회사명을 정규화한다")
    void processPendingReviewsTest() {
        // given
        ReviewStagingEntity review1 = new ReviewStagingEntity(
                "user1@test.com", "Samsung", "Title 1", "Content 1", "Tip 1", "5", "6m", "Java", "Backend"
        );
        ReviewStagingEntity review2 = new ReviewStagingEntity(
                "user2@test.com", "Apple", "Title 2", "Content 2", "Tip 2", "4", "3m", "Swift", "Mobile"
        );
        stagingRepository.saveAll(List.of(review1, review2));

        given(mcpClient.normalizeCompanyNames(anyList())).willReturn(Map.of(
                "Samsung", "삼성전자",
                "Apple", "Apple Inc."
        ));

        // when
        googleFormScheduler.processPendingReviews();

        // then
        List<ReviewStagingEntity> updatedReviews = stagingRepository.findAll();
        assertThat(updatedReviews).hasSize(2);

        ReviewStagingEntity updatedReview1 = updatedReviews.stream()
                .filter(r -> r.getRawCompanyName().equals("Samsung"))
                .findFirst().orElseThrow();
        assertThat(updatedReview1.getNormalizedCompanyName()).isEqualTo("삼성전자");
        assertThat(updatedReview1.getStatus()).isEqualTo(StagingStatus.ANALYZED);

        ReviewStagingEntity updatedReview2 = updatedReviews.stream()
                .filter(r -> r.getRawCompanyName().equals("Apple"))
                .findFirst().orElseThrow();
        assertThat(updatedReview2.getNormalizedCompanyName()).isEqualTo("Apple Inc.");
        assertThat(updatedReview2.getStatus()).isEqualTo(StagingStatus.ANALYZED);

        verify(mcpClient).normalizeCompanyNames(anyList());
    }
}
