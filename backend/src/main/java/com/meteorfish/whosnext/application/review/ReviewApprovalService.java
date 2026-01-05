package com.meteorfish.whosnext.application.review;

import com.meteorfish.whosnext.domain.review.JobCategory;
import com.meteorfish.whosnext.domain.review.Review;
import com.meteorfish.whosnext.infrastructure.external.ai.GeminiService;
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
    private final GeminiService geminiService;

    public ReviewApprovalService(ReviewStagingRepository stagingRepository, ReviewRepository reviewRepository,
                                 MemberRepository memberRepository, CompanyRepository companyRepository,
                                 GeminiService geminiService) {
        this.stagingRepository = stagingRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.companyRepository = companyRepository;
        this.geminiService = geminiService;
    }


    @Transactional
    public void approveReview(UUID reviewStagingId) {
        ReviewStagingEntity staging = stagingRepository.findById(reviewStagingId)
                .orElseThrow(() -> new IllegalArgumentException("대기 중인 리뷰가 없습니다."));

        MemberEntity member = memberRepository.findByEmail(staging.getRawMemberEmail())
                .orElseGet(this::getOrCreateAnonymousMember);

        String normalizedCompanyName = geminiService.normalizeCompanyName(staging.getRawCompanyName());

        logger.info("원본 기업명: " + staging.getRawCompanyName() + ", 정규화 기업명: " + normalizedCompanyName);

        CompanyEntity company = companyRepository.findByName(normalizedCompanyName)
                .orElseGet(() -> {
                    CompanyEntity newCompany = new CompanyEntity(UUID.randomUUID(), normalizedCompanyName, "정보없음", "정보없음");
                    return companyRepository.save(newCompany);
                });

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
