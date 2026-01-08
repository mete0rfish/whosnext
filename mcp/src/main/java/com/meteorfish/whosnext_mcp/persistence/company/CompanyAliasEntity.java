package com.meteorfish.whosnext_mcp.persistence.company;

import jakarta.persistence.*;

@Entity
@Table(name = "company_aliases")
public class CompanyAliasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String aliasName; // 예: "삼전", "Samsung"

    protected CompanyAliasEntity() {}

    public CompanyAliasEntity(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAliasName() {
        return aliasName;
    }
}
