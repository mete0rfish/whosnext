package com.meteorfish.whosnext.infrastructure.persistence.company;

import com.meteorfish.whosnext.domain.company.Company;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "companies")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String industry;
    private String location;

    protected CompanyEntity() {}

    public CompanyEntity(UUID id, String name, String industry, String location) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.location = location;
    }

    public static CompanyEntity from(Company company) {
        return new CompanyEntity(
                company.getId(),
                company.getName(),
                company.getIndustry(),
                company.getLocation()
        );
    }

    public Company toDomain() {
        return new Company(id, name, industry, location);
    }

    public void updateFromDomain(Company company) {
        this.name = company.getName();
        this.industry = company.getIndustry();
        this.location = company.getLocation();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIndustry() {
        return industry;
    }

    public String getLocation() {
        return location;
    }
}
