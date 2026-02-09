package com.meteorfish.whosnext.application.review;

import com.meteorfish.whosnext.domain.review.JobCategory;
import com.meteorfish.whosnext.domain.review.Review;
import com.meteorfish.whosnext.infrastructure.external.mcp.McpClient;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyAliasEntity;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyAliasRepository;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyEntity;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyRepository;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberEntity;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberRepository;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewRepository;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ReviewApprovalService {
    private final Logger logger = Logger.getLogger(ReviewApprovalService.class.getName());

    private final ReviewStagingRepository stagingRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final CompanyAliasRepository companyAliasRepository;
    private final McpClient mcpClient;

    @Transactional
    public void approveReview(UUID reviewStagingId) {
        ReviewStagingEntity staging = stagingRepository.findById(reviewStagingId)
                .orElseThrow(() -> new IllegalArgumentException("대기 중인 리뷰가 없습니다."));

        MemberEntity member = memberRepository.findByEmail(staging.getRawMemberEmail())
                .orElseGet(this::getOrCreateAnonymousMember);

        String targetCompanyName = staging.getNormalizedCompanyName();
        if (targetCompanyName == null || targetCompanyName.isBlank()) {
            targetCompanyName = staging.getRawCompanyName();
        }

        CompanyEntity company = getOrCreateCompany(staging.getRawCompanyName(), targetCompanyName);

        JobCategory jobCategory = parseJobCategory(staging.getJobCategory());

        Review review = new Review(
                null,
                member.toDomain(),
                company.toDomain(),
                staging.getTitle(),
                staging.getContent(),
                staging.getTips(),
                Integer.parseInt(staging.getRating()),
                staging.getPreparationPeriod(),
                staging.getTechStack(),
                jobCategory,
                LocalDateTime.now()
        );

        reviewRepository.save(ReviewEntity.from(review, member, company));

        staging.approve();
    }

    private CompanyEntity getOrCreateCompany(String rawName, String normalizedNameHint) {
        String trimmedRawName = rawName.trim();
        String trimmedNormalizedName = (normalizedNameHint != null) ? normalizedNameHint.trim() : trimmedRawName;

        // 1. 원본 이름으로 회사 찾기
        return companyRepository.findByName(trimmedRawName)
                .or(() -> {
                    return companyAliasRepository.findByAliasName(trimmedRawName)
                            .map(CompanyAliasEntity::getCompany);
                })
                .orElseGet(() -> {
                    String finalNormalizedName = trimmedNormalizedName;

                    if (finalNormalizedName.equals(trimmedRawName)) {
                         try {
                             var result = mcpClient.normalizeCompanyNames(Collections.singletonList(trimmedRawName));
                             if (result != null && result.containsKey(trimmedRawName)) {
                                 finalNormalizedName = result.get(trimmedRawName);
                             }
                         } catch (Exception e) {
                             logger.warning("MCP call failed during approval: " + e.getMessage());
                         }
                    }

                    String effectiveName = finalNormalizedName; // lambda effectively final

                    CompanyEntity company = companyRepository.findByName(effectiveName)
                            .orElseGet(() -> {
                                CompanyEntity newCompany = new CompanyEntity(UUID.randomUUID(), effectiveName, "정보없음", "정보없음");
                                return companyRepository.save(newCompany);
                            });

                    if (!trimmedRawName.equalsIgnoreCase(effectiveName)) {
                        companyAliasRepository.save(new CompanyAliasEntity(trimmedRawName, company));
                        logger.info("Alias Cached: " + trimmedRawName + " -> " + effectiveName);
                    }

                    return company;
                });
    }

    private MemberEntity getOrCreateAnonymousMember() {
        String anonymousEmail = "anonymous@whosnext.com";
        return memberRepository.findByEmail(anonymousEmail)
                .orElseGet(() -> {
                    MemberEntity anonymous = new MemberEntity(null, anonymousEmail, "익명", "anonymous", "system", "USER");
                    return memberRepository.save(anonymous);
                });
    }

    private JobCategory parseJobCategory(String rawCategory) {
        if (rawCategory == null || rawCategory.isBlank()) {
            return JobCategory.OTHER;
        }
        return Arrays.stream(JobCategory.values())
                .filter(c -> c.name().equalsIgnoreCase(rawCategory) || c.getDescription().equals(rawCategory))
                .findFirst()
                .orElse(JobCategory.OTHER);
    }
}
