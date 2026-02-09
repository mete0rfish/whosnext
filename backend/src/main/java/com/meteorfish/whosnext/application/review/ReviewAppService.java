package com.meteorfish.whosnext.application.review;

import com.meteorfish.whosnext.api.review.ReviewCreateRequest;
import com.meteorfish.whosnext.api.review.ReviewUpdateRequest;
import com.meteorfish.whosnext.domain.review.Review;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyEntity;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyRepository;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberEntity;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberRepository;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewAppService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public UUID create(Long memberId, ReviewCreateRequest request) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        CompanyEntity company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new IllegalArgumentException("회사를 찾을 수 없습니다."));

        Review review = new Review(null, member.toDomain(), company.toDomain(),
                request.title(), request.content(),
                request.tips(), request.rating(),
                request.preparationPeriod(), request.techStack(), request.jobCategory(),
                LocalDateTime.now());

        ReviewEntity entity = ReviewEntity.from(review, member, company);
        return reviewRepository.save(entity).toDomain().getId();
    }

    @Transactional(readOnly = true)
    public Review getReview(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."))
                .toDomain();
    }

    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(ReviewEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(UUID reviewId, Long memberId, ReviewUpdateRequest request) {
        ReviewEntity entity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // 권한 확인: 작성자 본인만 수정 가능
        if (!entity.toDomain().getMember().getId().equals(memberId)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        Review review = entity.toDomain();
        review.update(request.title(), request.content(), request.tips(), request.rating(),
                request.preparationPeriod(), request.techStack(), request.jobCategory());

        entity.updateFromDomain(review);
    }

    @Transactional
    public void delete(UUID reviewId, Long memberId) {
        ReviewEntity entity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!entity.toDomain().getMember().getId().equals(memberId)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        reviewRepository.delete(entity);
    }
}
