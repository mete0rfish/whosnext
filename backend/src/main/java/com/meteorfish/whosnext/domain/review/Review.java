package com.meteorfish.whosnext.domain.review;

import com.meteorfish.whosnext.domain.company.Company;
import com.meteorfish.whosnext.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Review {
    private final UUID id;
    private final Member member;
    private final Company company;
    private String title;
    private String content;
    private String tips;
    private int rating;
    private String preparationPeriod; // 취준 기간
    private String techStack; // 기술 스택
    private JobCategory jobCategory; // 직군
    private final LocalDateTime createdAt;

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
}
