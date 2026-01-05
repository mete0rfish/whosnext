package com.meteorfish.whosnext.infrastructure.external.google;

import com.meteorfish.whosnext.infrastructure.persistence.config.SyncMetadataEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GoogleFormScheduler {

    private static final String SYNC_KEY = "GOOGLE_SHEETS_REVIEW";
    private final SyncMetadataRepository metadataRepository;
    private final ReviewStagingRepository reviewStagingRepository;
    private final GoogleSheetsService googleSheetsService;

    public GoogleFormScheduler(GoogleSheetsService googleSheetsService,
                               ReviewStagingRepository stagingRepository,
                               SyncMetadataRepository metadataRepository) {
        this.googleSheetsService = googleSheetsService;
        this.reviewStagingRepository = stagingRepository;
        this.metadataRepository = metadataRepository;
    }

    @Scheduled(fixedDelay = 3600000)
    @Transactional
    public void fetchGoogleFormData() {
        SyncMetadataEntity metadata = metadataRepository.findById(SYNC_KEY)
                .orElseGet(() -> metadataRepository.save(new SyncMetadataEntity(SYNC_KEY, 1)));

        int lastReadRow = metadata.getLastRowIndex();

        List<GoogleFormRow> newRows = googleSheetsService.getNewResponses(lastReadRow);

        if (newRows.isEmpty()) {
            return;
        }

        for (GoogleFormRow row : newRows) {
            ReviewStagingEntity staging = new ReviewStagingEntity(
                    row.companyName(),
                    row.email(),
                    row.rating(),
                    row.title(),
                    row.content(),
                    row.tips()
            );
            reviewStagingRepository.save(staging);
        }

        int updatedLastRow = lastReadRow + newRows.size();
        metadata.updateLastRow(updatedLastRow);
    }
}
