package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.services.ReviewService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
