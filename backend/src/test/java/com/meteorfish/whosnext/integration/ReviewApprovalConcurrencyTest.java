package com.meteorfish.whosnext.integration;

import com.meteorfish.whosnext.application.review.ReviewApprovalService;
import com.meteorfish.whosnext.infrastructure.external.mcp.McpClient;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyRepository;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ReviewApprovalConcurrencyTest {

    @Autowired
    private ReviewApprovalService reviewApprovalService;

    @Autowired
    private ReviewStagingRepository stagingRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @MockBean
    private McpClient mcpClient;

    @AfterEach
    void tearDown() {
        stagingRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 10개의 리뷰 승인 요청이 들어와도 회사는 하나만 생성되어야 한다")
    void concurrentReviewApprovalTest() throws InterruptedException {
        // given
        int threadCount = 10;
        String companyName = "Concurrent Corp";
        
        // 10개의 대기 중인 리뷰 생성
        List<UUID> stagingIds = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            ReviewStagingEntity staging = new ReviewStagingEntity(
                    "user" + i + "@test.com",
                    companyName,
                    "Title " + i,
                    "Content " + i,
                    "Tip " + i,
                    "5",
                    "6m",
                    "Java",
                    "Backend"
            );
            // 정규화된 이름도 동일하게 설정 (배치가 돌았다고 가정)
            staging.updateNormalizedCompanyName(companyName);
            stagingRepository.save(staging);
            stagingIds.add(staging.getId());
        }

        // McpClient Mocking (혹시 호출될 경우를 대비)
        given(mcpClient.normalizeCompanyNames(anyList())).willReturn(Collections.emptyMap());

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (UUID id : stagingIds) {
            executorService.submit(() -> {
                try {
                    reviewApprovalService.approveReview(id);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기

        // then
        // 1. 회사는 단 1개만 생성되어야 함
        long companyCount = companyRepository.count();
        assertThat(companyCount).as("회사가 중복 생성되었습니다!").isEqualTo(1);

        // 2. 모든 요청이 성공해야 함 (실패가 없어야 함)
        // 만약 낙관적 락이나 Unique 제약 조건 예외 처리가 안 되어 있다면 실패할 수 있음
        assertThat(failCount.get()).as("일부 요청이 실패했습니다.").isEqualTo(0);
        assertThat(successCount.get()).isEqualTo(threadCount);

        ConcurrentHashMap
    }
}
