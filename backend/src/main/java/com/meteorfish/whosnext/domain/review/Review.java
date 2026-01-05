package com.meteorfish.whosnext.domain.review;

import java.time.LocalDateTime;
import java.util.UUID;

public class Review {
    private final UUID id;
    private final Long memberId;
    private final UUID companyId;
    private String title;
    private String content;
    private String tips;
    private int rating;
    private String preparationPeriod; // 취준 기간
    private String techStack; // 기술 스택
    private JobCategory jobCategory; // 직군
    private final LocalDateTime createdAt;

    public Review(UUID id, Long memberId, UUID companyId, String title, String content, String tips, int rating, String preparationPeriod, String techStack, JobCategory jobCategory, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.companyId = companyId;
        this.title = title;
        this.content = content;
        this.tips = tips;
        this.rating = rating;
        this.preparationPeriod = preparationPeriod;
        this.techStack = techStack;
        this.jobCategory = jobCategory;
        this.createdAt = createdAt;
    }

    public void update(String title, String content, String tips, int rating, String preparationPeriod, String techStack, JobCategory jobCategory) {
        validateRating(rating);
        this.title = title;
        this.content = content;
        this.tips = tips;
        this.rating = rating;
        this.preparationPeriod = preparationPeriod;
        this.techStack = techStack;
        this.jobCategory = jobCategory;
    }

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("평점은 1~5점 사이여야 합니다.");
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

    public String getPreparationPeriod() {
        return preparationPeriod;
    }

    public String getTechStack() {
        return techStack;
    }

    public JobCategory getJobCategory() {
        return jobCategory;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
