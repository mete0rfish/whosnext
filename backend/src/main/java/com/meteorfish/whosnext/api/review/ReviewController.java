package com.meteorfish.whosnext.api.review;

import com.meteorfish.whosnext.application.review.ReviewAppService;
import com.meteorfish.whosnext.domain.review.Review;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@Tag(name = "Review API", description = "취업 후기 관리 API")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewAppService reviewAppService;

    public ReviewController(ReviewAppService reviewAppService) {
        this.reviewAppService = reviewAppService;
    }

    @Operation(summary = "후기 작성", description = "새로운 취업 후기를 작성합니다.")
    @PostMapping
    public ResponseEntity<UUID> create(
            @RequestBody ReviewCreateRequest request,
            @AuthenticationPrincipal OAuth2User principal) {
        Long memberId = Long.valueOf(Objects.requireNonNull(principal.getAttribute("id")));
        UUID reviewId = reviewAppService.create(memberId, request);
        return ResponseEntity.ok(reviewId);
    }

    @Operation(summary = "후기 상세 조회", description = "ID로 특정 후기의 상세 내용을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getOne(@PathVariable UUID id) {
        Review review = reviewAppService.getReview(id);
        return ResponseEntity.ok(ReviewResponse.from(review));
    }
}
