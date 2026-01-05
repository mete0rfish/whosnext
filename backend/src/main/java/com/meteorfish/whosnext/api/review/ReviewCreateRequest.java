package com.meteorfish.whosnext.api.review;

import com.meteorfish.whosnext.domain.review.JobCategory;

import java.util.UUID;

public record ReviewCreateRequest(
        UUID companyId,
        String title,
        String content,
        String tips,
        int rating,
        String preparationPeriod,
        String techStack,
        JobCategory jobCategory
) {
}
