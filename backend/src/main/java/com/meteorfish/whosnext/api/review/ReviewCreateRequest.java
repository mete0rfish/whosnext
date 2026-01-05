package com.meteorfish.whosnext.api.review;

import java.util.UUID;

public record ReviewCreateRequest(
        UUID companyId,
        String title,
        String content,
        String tips,
        int rating
) {
}
