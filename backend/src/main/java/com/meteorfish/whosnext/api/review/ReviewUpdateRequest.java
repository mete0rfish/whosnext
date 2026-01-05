package com.meteorfish.whosnext.api.review;

public record ReviewUpdateRequest(
        String title,
        String content,
        String tips,
        int rating
) {
}
