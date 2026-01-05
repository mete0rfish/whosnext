package com.meteorfish.whosnext.api.review;

import com.meteorfish.whosnext.application.review.ReviewAppService;
import com.meteorfish.whosnext.domain.review.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewAppService reviewAppService;

    public ReviewController(ReviewAppService reviewAppService) {
        this.reviewAppService = reviewAppService;
    }

    @PostMapping
    public ResponseEntity<UUID> create(
            @RequestBody ReviewCreateRequest request,
            @AuthenticationPrincipal OAuth2User principal) {
        Long memberId = Long.valueOf(Objects.requireNonNull(principal.getAttribute("id")));
        UUID reviewId = reviewAppService.create(memberId, request);
        return ResponseEntity.ok(reviewId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getOne(@PathVariable UUID id) {
        Review review = reviewAppService.getReview(id);
        return ResponseEntity.ok(ReviewResponse.from(review));
    }
}
