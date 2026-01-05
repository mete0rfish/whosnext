package com.meteorfish.whosnext.infrastructure.persistence.review;

import com.meteorfish.whosnext.domain.review.JobCategory;
import com.meteorfish.whosnext.domain.review.Review;
import com.meteorfish.whosnext.infrastructure.persistence.company.CompanyEntity;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", columnDefinition = "uuid")
    private CompanyEntity company;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String tips;

    private int rating;

    @Column(columnDefinition = "TEXT")
    private String preparationPeriod;

    @Column(columnDefinition = "TEXT")
    private String techStack;

    @Enumerated(EnumType.STRING)
    private JobCategory jobCategory;

    private LocalDateTime createdAt;

    protected ReviewEntity() {}

    private ReviewEntity(UUID id, MemberEntity member, CompanyEntity company, String title, String content, String tips, int rating, String preparationPeriod, String techStack, JobCategory jobCategory, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.company = company;
        this.title = title;
        this.content = content;
        this.tips = tips;
        this.rating = rating;
        this.preparationPeriod = preparationPeriod;
        this.techStack = techStack;
        this.jobCategory = jobCategory;
        this.createdAt = createdAt;
    }

    public static ReviewEntity from(Review review, MemberEntity member, CompanyEntity company) {
        return new ReviewEntity(
                review.getId(),
                member,
                company,
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

    public Review toDomain() {
        return new Review(
                id,
                member.getId(),
                company.getId(),
                title,
                content,
                tips,
                rating,
                preparationPeriod,
                techStack,
                jobCategory,
                createdAt
        );
    }

    public void updateFromDomain(Review review) {
        this.title = review.getTitle();
        this.content = review.getContent();
        this.tips = review.getTips();
        this.rating = review.getRating();
        this.preparationPeriod = review.getPreparationPeriod();
        this.techStack = review.getTechStack();
        this.jobCategory = review.getJobCategory();
    }
}
