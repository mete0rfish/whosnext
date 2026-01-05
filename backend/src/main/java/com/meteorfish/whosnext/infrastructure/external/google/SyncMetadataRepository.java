package com.meteorfish.whosnext.infrastructure.external.google;

import com.meteorfish.whosnext.infrastructure.persistence.config.SyncMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncMetadataRepository extends JpaRepository<SyncMetadataEntity, String> {
}
