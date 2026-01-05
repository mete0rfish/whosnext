package com.meteorfish.whosnext.application.review;

import com.meteorfish.whosnext.domain.review.Review;
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
import java.util.UUID;

@Service
public class ReviewApprovalService {
    private final ReviewStagingRepository stagingRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;

    public ReviewApprovalService(ReviewStagingRepository stagingRepository, ReviewRepository reviewRepository,
                                 MemberRepository memberRepository, CompanyRepository companyRepository) {
        this.stagingRepository = stagingRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.companyRepository = companyRepository;
    }


    @Transactional
    public void approveReview(UUID reviewStagingId) {
        ReviewStagingEntity staging = stagingRepository.findById(reviewStagingId)
                .orElseThrow(() -> new IllegalArgumentException("대기 중인 리뷰가 없습니다."));

        MemberEntity member = memberRepository.findByEmail(staging.getRawMemberEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        CompanyEntity company = companyRepository.findByName(staging.getRawCompanyName())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회사입니다. 먼저 회사를 등록하세요."));

        Review review = new Review(
                null,
                member.getId(),
                company.getId(),
                staging.getTitle(),
                staging.getContent(),
                staging.getTips(),
                Integer.parseInt(staging.getRating()),
                LocalDateTime.now()
        );

        reviewRepository.save(ReviewEntity.from(review, member, company));

        staging.approve();
    }
}
