package com.meteorfish.whosnext.infrastructure.persistence.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewStagingRepository extends JpaRepository<ReviewStagingEntity, UUID> {
    List<ReviewStagingEntity> findByStatus(StagingStatus status);
}
