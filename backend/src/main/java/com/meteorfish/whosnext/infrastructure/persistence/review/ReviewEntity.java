package com.meteorfish.whosnext.infrastructure.persistence.review;

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
    private String content;
    private String tips;
    private int rating;
    private LocalDateTime createdAt;

    protected ReviewEntity() {}

    private ReviewEntity(UUID id, MemberEntity member, CompanyEntity company, String title, String content, String tips, int rating, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.company = company;
        this.title = title;
        this.content = content;
        this.tips = tips;
        this.rating = rating;
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
                createdAt
        );
    }

    public void updateFromDomain(Review review) {
        this.title = review.getTitle();
        this.content = review.getContent();
        this.tips = review.getTips();
        this.rating = review.getRating();
    }
}
