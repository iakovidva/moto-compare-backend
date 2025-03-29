package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.mappers.ReviewMapper;
import com.vaiak.moto_compare.models.Review;
import com.vaiak.moto_compare.services.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/motorcycles")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping("/{motorcycleId}/reviews")
    public List<ReviewResponseDTO> getMotorcycleReviews(@PathVariable Long motorcycleId) {
        List<Review> motorcycleReviews = service.getMotorcycleReviews(motorcycleId);
        List<ReviewResponseDTO> reviews = motorcycleReviews.stream().map(ReviewMapper::toResponseDTO).toList();
    System.out.println(motorcycleReviews);
    System.out.println(reviews);
        return reviews;
    }

    @PostMapping("/{motorcycleId}/reviews")
    public ReviewResponseDTO createReview(@PathVariable Long motorcycleId,
                                          @RequestBody ReviewRequestDTO reviewRequestDTO) {
        System.out.println("---> The motorcycle id: " + motorcycleId);
        System.out.println("---> The review: " + reviewRequestDTO);
        Review savedReview = service.saveReview(motorcycleId, reviewRequestDTO);
        return ReviewMapper.toResponseDTO(savedReview);
    }
}
