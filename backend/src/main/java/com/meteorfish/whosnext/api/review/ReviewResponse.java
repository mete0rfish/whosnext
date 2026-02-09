package com.meteorfish.whosnext.api.review;

import com.meteorfish.whosnext.domain.review.JobCategory;
import com.meteorfish.whosnext.domain.review.Review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        Long memberId,
        UUID companyId,
        String companyName,
        String title,
        String content,
        String tips,
        int rating,
        String preparationPeriod,
        String techStack,
        JobCategory jobCategory,
        LocalDateTime createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getMember().getId(),
                review.getCompany().getId(),
                review.getCompany().getName(),
                review.getTitle(),
                review.getContent(),
                review.getTips(),
                review.getRating(),
                review.getPreparationPeriod(),
                review.getTechStack(),
                review.getJobCategory(),
                review.getCreatedAt()
        );
    }
}
