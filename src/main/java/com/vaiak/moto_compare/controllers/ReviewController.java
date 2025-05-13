package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/motorcycles")
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<ReviewResponseDTO> createReview(@PathVariable Long motorcycleId,
                                          @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewResponseDTO review = service.saveReview(motorcycleId, reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }
}
