package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.services.ReviewService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/motorcycles")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping("/{motorcycleId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getMotorcycleReviews(@PathVariable Long motorcycleId) {
        List<ReviewResponseDTO> motorcycleReviews = service.getMotorcycleReviews(motorcycleId);
        return ResponseEntity.ok(motorcycleReviews);
    }

    @PostMapping("/{motorcycleId}/reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponseDTO> createReview(@PathVariable Long motorcycleId,
                                                          @RequestBody ReviewRequestDTO reviewRequestDTO,
                                                          Authentication auth) {
        ReviewResponseDTO review = service.saveReview(motorcycleId, reviewRequestDTO, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping("/{motorcycleId}/reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long motorcycleId,
                                                          @RequestBody ReviewRequestDTO reviewRequestDTO,
                                                          Authentication auth) {
        ReviewResponseDTO review = service.updateReview(motorcycleId, reviewRequestDTO, auth);
        return ResponseEntity.status(HttpStatus.OK).body(review);
    }

    @DeleteMapping("/{motorcycleId}/reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long motorcycleId, Authentication auth) {
        service.deleteReview(motorcycleId, auth);
        return ResponseEntity.noContent().build();
    }
}
