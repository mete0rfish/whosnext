package com.meteorfish.whosnext.api.admin;

import com.meteorfish.whosnext.infrastructure.external.google.GoogleFormScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Sync", description = "관리자 데이터 동기화 API")
@RestController
@RequestMapping("/api/admin/sync")
public class AdminGoogleSyncController {

    private final GoogleFormScheduler googleFormScheduler;

    public AdminGoogleSyncController(GoogleFormScheduler googleFormScheduler) {
        this.googleFormScheduler = googleFormScheduler;
    }

    @Operation(summary = "구글 폼 동기화 트리거", description = "구글 시트에서 최신 데이터를 수동으로 가져와 Staging 영역에 저장합니다.")
    @PostMapping("/google-forms")
    public ResponseEntity<String> triggerGoogleFormSync() {
        googleFormScheduler.fetchGoogleFormData();
        return ResponseEntity.ok("Google Form sync triggered successfully.");
    }
}
