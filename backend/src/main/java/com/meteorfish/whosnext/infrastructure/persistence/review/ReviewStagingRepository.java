package com.meteorfish.whosnext.infrastructure.persistence.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewStagingRepository extends JpaRepository<ReviewStagingEntity, UUID> {
}
