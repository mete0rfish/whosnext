package com.meteorfish.whosnext.integration;

import com.meteorfish.whosnext.infrastructure.external.google.GoogleFormRow;
import com.meteorfish.whosnext.infrastructure.external.google.GoogleFormScheduler;
import com.meteorfish.whosnext.infrastructure.external.google.GoogleSheetsService;
import com.meteorfish.whosnext.infrastructure.external.google.SyncMetadataRepository;
import com.meteorfish.whosnext.infrastructure.persistence.config.SyncMetadataEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class GoogleFormSyncIntegrationTest {

    @Autowired private GoogleFormScheduler googleFormScheduler;

    @Autowired private ReviewStagingRepository stagingRepository;

    @Autowired private SyncMetadataRepository metadataRepository;

    @MockitoBean
    private GoogleSheetsService googleSheetsService;

    @Test
    @DisplayName("구글 시트로부터 새로운 데이터를 성공적으로 동기화하고 인덱스를 업데이트한다")
    void syncGoogleFormDataTest() {
        // given
        String syncKey = "GOOGLE_SHEETS_REVIEW";
        int initialRowIndex = 1;
        metadataRepository.save(new SyncMetadataEntity(syncKey, initialRowIndex));

        List<GoogleFormRow> mockRows = List.of(
                new GoogleFormRow("user1@test.com", "Samsung", "Title 1", "Content 1", "Tip 1", "5"),
                new GoogleFormRow("user2@test.com", "Naver", "Title 2", "Content 2", "Tip 2", "4")
        );

        given(googleSheetsService.getNewResponses(initialRowIndex)).willReturn(mockRows);

        // when
        googleFormScheduler.fetchGoogleFormData();

        // then
        List<ReviewStagingEntity> savedStaging = stagingRepository.findAll();
        assertThat(savedStaging).hasSize(2);
        assertThat(savedStaging.get(0).getRawMemberEmail()).isEqualTo("user1@test.com");
        assertThat(savedStaging.get(1).getRawCompanyName()).isEqualTo("Naver");

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
}
