package com.meteorfish.whosnext.application.review;

import com.meteorfish.whosnext.domain.review.JobCategory;
import com.meteorfish.whosnext.domain.review.Review;
import com.meteorfish.whosnext.infrastructure.external.ai.GeminiService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class ReviewApprovalService {
    private final Logger logger = Logger.getLogger(ReviewApprovalService.class.getName());

    private final ReviewStagingRepository stagingRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final CompanyAliasRepository companyAliasRepository;
    private final GeminiService geminiService;

    public ReviewApprovalService(ReviewStagingRepository stagingRepository, ReviewRepository reviewRepository,
                                 MemberRepository memberRepository, CompanyRepository companyRepository,
                                 CompanyAliasRepository companyAliasRepository, GeminiService geminiService) {
        this.stagingRepository = stagingRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.companyRepository = companyRepository;
        this.companyAliasRepository = companyAliasRepository;
        this.geminiService = geminiService;
    }


    @Transactional
    public void approveReview(UUID reviewStagingId) {
        ReviewStagingEntity staging = stagingRepository.findById(reviewStagingId)
                .orElseThrow(() -> new IllegalArgumentException("대기 중인 리뷰가 없습니다."));

        MemberEntity member = memberRepository.findByEmail(staging.getRawMemberEmail())
                .orElseGet(this::getOrCreateAnonymousMember);

        CompanyEntity company = getOrCreateCompany(staging.getRawCompanyName());

        JobCategory jobCategory = parseJobCategory(staging.getJobCategory());

        Review review = new Review(
                null,
                member.getId(),
                company.getId(),
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

    private CompanyEntity getOrCreateCompany(String rawName) {
        String trimmedName = rawName.trim();

        return companyRepository.findByName(trimmedName)
                .or(() -> {
                    return companyAliasRepository.findByAliasName(trimmedName)
                            .map(CompanyAliasEntity::getCompany);
                })
                .orElseGet(() -> {
                    logger.info("Cache Miss: Gemini 호출 - " + trimmedName);
                    String normalizedName = geminiService.normalizeCompanyName(trimmedName);

                    CompanyEntity company = companyRepository.findByName(normalizedName)
                            .orElseGet(() -> {
                                CompanyEntity newCompany = new CompanyEntity(UUID.randomUUID(), normalizedName, "정보없음", "정보없음");
                                return companyRepository.save(newCompany);
                            });

                    if (!trimmedName.equalsIgnoreCase(normalizedName)) {
                        companyAliasRepository.save(new CompanyAliasEntity(trimmedName, company));
                        logger.info("Alias Cached: " + trimmedName + " -> " + normalizedName);
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
