package com.meteorfish.whosnext.infrastructure.persistence.company;

import jakarta.persistence.*;

@Entity
@Table(name = "company_aliases")
public class CompanyAliasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String aliasName; // 예: "삼전", "Samsung"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company; // 예: 삼성전자 엔티티

    protected CompanyAliasEntity() {}

    public CompanyAliasEntity(String aliasName, CompanyEntity company) {
        this.aliasName = aliasName;
        this.company = company;
    }

    public String getAliasName() {
        return aliasName;
    }

    public CompanyEntity getCompany() {
        return company;
    }
}
