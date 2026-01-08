package com.meteorfish.whosnext_mcp.persistence.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyAliasRepository extends JpaRepository<CompanyAliasEntity, Long> {
    Optional<CompanyAliasEntity> findByAliasName(String aliasName);
}
