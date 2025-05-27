package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.security.jwt.JwtTokenProvider;
import com.vaiak.moto_compare.security.models.User;
import com.vaiak.moto_compare.services.ReviewService;
import java.util.List;

import com.vaiak.moto_compare.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
                                                          @RequestHeader("Authorization") String authHeader) {
        ReviewResponseDTO review = service.saveReview(motorcycleId, reviewRequestDTO, authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }
}
