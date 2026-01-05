package com.meteorfish.whosnext.infrastructure.persistence.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {
}
