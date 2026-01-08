package com.meteorfish.whosnext.infrastructure.external.google;

import com.meteorfish.whosnext.infrastructure.external.mcp.McpClient;
import com.meteorfish.whosnext.infrastructure.persistence.config.SyncMetadataEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingEntity;
import com.meteorfish.whosnext.infrastructure.persistence.review.ReviewStagingRepository;
import com.meteorfish.whosnext.infrastructure.persistence.review.StagingStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleFormScheduler {

    private static final Logger log = LoggerFactory.getLogger(GoogleFormScheduler.class);
    private static final String SYNC_KEY = "GOOGLE_SHEETS_REVIEW";
    
    private final SyncMetadataRepository metadataRepository;
    private final ReviewStagingRepository reviewStagingRepository;
    private final GoogleSheetsService googleSheetsService;
    private final McpClient mcpClient;

    public GoogleFormScheduler(GoogleSheetsService googleSheetsService,
                               ReviewStagingRepository stagingRepository,
                               SyncMetadataRepository metadataRepository,
                               McpClient mcpClient) {
        this.googleSheetsService = googleSheetsService;
        this.reviewStagingRepository = stagingRepository;
        this.metadataRepository = metadataRepository;
        this.mcpClient = mcpClient;
    }

    @Scheduled(fixedDelay = 3600000)
    @Transactional
    public void fetchGoogleFormData() {
        log.info("Starting Google Form data fetch...");
        SyncMetadataEntity metadata = metadataRepository.findById(SYNC_KEY)
                .orElseGet(() -> metadataRepository.save(new SyncMetadataEntity(SYNC_KEY, 1)));

        int lastReadRow = metadata.getLastRowIndex();

        List<GoogleFormRow> newRows = googleSheetsService.getNewResponses(lastReadRow);

        if (newRows.isEmpty()) {
            log.info("No new rows found.");
            return;
        }

        for (GoogleFormRow row : newRows) {
            ReviewStagingEntity staging = new ReviewStagingEntity(
                    row.email(),
                    row.companyName(),
                    row.title(),
                    row.content(),
                    row.tips(),
                    row.rating(),
                    row.preparationPeriod(),
                    row.techStack(),
                    row.jobCategory()
            );
            reviewStagingRepository.save(staging);
        }

        int updatedLastRow = lastReadRow + newRows.size();
        metadata.updateLastRow(updatedLastRow);
        log.info("Fetched {} new rows. Updated last row index to {}.", newRows.size(), updatedLastRow);
    }

    @Scheduled(fixedDelay = 600000)
    @Transactional
    public void processPendingReviews() {
        log.info("Starting processing of PENDING reviews...");
        List<ReviewStagingEntity> pendingReviews = reviewStagingRepository.findByStatus(StagingStatus.PENDING);

        if (pendingReviews.isEmpty()) {
            log.info("No pending reviews to process.");
            return;
        }

        List<String> rawCompanyNames = pendingReviews.stream()
                .map(ReviewStagingEntity::getRawCompanyName)
                .distinct()
                .toList();

        log.info("Sending {} unique company names to MCP for normalization...", rawCompanyNames.size());

        Map<String, String> normalizedMap = mcpClient.normalizeCompanyNames(rawCompanyNames);

        int updatedCount = 0;
        for (ReviewStagingEntity review : pendingReviews) {
            String rawName = review.getRawCompanyName();
            if (normalizedMap.containsKey(rawName)) {
                String normalizedName = normalizedMap.get(rawName);
                review.updateNormalizedCompanyName(normalizedName);
                updatedCount++;
            } else {
                log.warn("Normalization failed for company: {}", rawName);
            }
        }
        
        log.info("Finished processing. Updated {} out of {} reviews.", updatedCount, pendingReviews.size());
    }
}
