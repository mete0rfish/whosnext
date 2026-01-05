package com.meteorfish.whosnext.infrastructure.persistence.config;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sync_metadata")
public class SyncMetadataEntity {
    @Id
    private String syncKey;
    private int lastRowIndex;

    protected SyncMetadataEntity() {}

    public SyncMetadataEntity(String syncKey, int lastRowIndex) {
        this.syncKey = syncKey;
        this.lastRowIndex = lastRowIndex;
    }

    public void updateLastRow(int newLastRow) {
        this.lastRowIndex = newLastRow;
    }

    public String getSyncKey() {
        return syncKey;
    }

    public int getLastRowIndex() {
        return lastRowIndex;
    }
}
