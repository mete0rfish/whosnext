package com.meteorfish.whosnext.domain.review;

import java.time.LocalDateTime;
import java.util.UUID;

public class Review {
    private final UUID id;
    private final Long memberId;
    private final UUID companyId;
    private final String title;
    private final String content;
    private final String tips;
    private final int rating;
    private final LocalDateTime createdAt;

    public Review(UUID id, Long memberId, UUID companyId, String title, String content, String tips, int rating, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.companyId = companyId;
        this.title = title;
        this.content = content;
        this.tips = tips;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTips() {
        return tips;
    }

    public int getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
