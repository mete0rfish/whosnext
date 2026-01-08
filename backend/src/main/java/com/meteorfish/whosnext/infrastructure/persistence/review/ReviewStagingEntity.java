package com.meteorfish.whosnext.infrastructure.persistence.review;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "review_staging")
public class ReviewStagingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String rawMemberEmail;
    private String rawCompanyName;
    private String normalizedCompanyName;

    private String title;
    @Column(columnDefinition = "text")
    private String content;
    @Column(columnDefinition = "text")
    private String tips;
    private String rating;

    private String preparationPeriod;
    private String techStack;
    private String jobCategory;

    @Enumerated(EnumType.STRING)
    private StagingStatus status;

    private LocalDateTime createdAt;

    protected ReviewStagingEntity() {}

    public ReviewStagingEntity(String rawMemberEmail, String rawCompanyName, String title, String content, String tips, String rating, String preparationPeriod, String techStack, String jobCategory) {
        this.rawMemberEmail = rawMemberEmail;
        this.rawCompanyName = rawCompanyName;
        this.title = title;
        this.content = content;
        this.tips = tips;
        this.rating = rating;
        this.preparationPeriod = preparationPeriod;
        this.techStack = techStack;
        this.jobCategory = jobCategory;
        this.status = StagingStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void updateNormalizedCompanyName(String normalizedName) {
        this.normalizedCompanyName = normalizedName;
        this.status = StagingStatus.ANALYZED;
    }

    public void approve() { this.status = StagingStatus.APPROVED; }
    public void reject() { this.status = StagingStatus.REJECTED; }

    public UUID getId() { return id; }
    public String getRawMemberEmail() { return rawMemberEmail; }
    public String getRawCompanyName() { return rawCompanyName; }
    public String getNormalizedCompanyName() { return normalizedCompanyName; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTips() { return tips; }
    public String getRating() { return rating; }
    public String getPreparationPeriod() { return preparationPeriod; }
    public String getTechStack() { return techStack; }
    public String getJobCategory() { return jobCategory; }
    public StagingStatus getStatus() { return status; }
}
