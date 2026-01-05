package com.meteorfish.whosnext.api.admin;

import com.meteorfish.whosnext.application.review.ReviewApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Admin Review", description = "관리자 리뷰 관리 API")
@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final ReviewApprovalService reviewApprovalService;

    public AdminReviewController(ReviewApprovalService reviewApprovalService) {
        this.reviewApprovalService = reviewApprovalService;
    }

    @Operation(summary = "리뷰 승인", description = "대기 중인(PENDING) 리뷰를 승인하여 정식 리뷰로 등록합니다.")
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveReview(
            @Parameter(description = "승인할 리뷰의 Staging ID", required = true)
            @PathVariable UUID id
    ) {
        reviewApprovalService.approveReview(id);
        return ResponseEntity.ok("Review approved successfully.");
    }
}
