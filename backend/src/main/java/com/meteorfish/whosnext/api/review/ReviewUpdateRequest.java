package com.meteorfish.whosnext.api.review;

import com.meteorfish.whosnext.domain.review.JobCategory;

public record ReviewUpdateRequest(
        String title,
        String content,
        String tips,
        int rating,
        String preparationPeriod,
        String techStack,
        JobCategory jobCategory
) {
}
