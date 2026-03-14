package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.mappers.ReviewMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.Review;
import com.vaiak.moto_compare.repositories.MotorcycleRepository;
import com.vaiak.moto_compare.repositories.ReviewRepository;
import com.vaiak.moto_compare.security.models.User;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final UserService userService;
    private final MeterRegistry meterRegistry;

    public ReviewService(ReviewRepository reviewRepository,
                         MotorcycleRepository motorcycleRepository,
                         UserService userService,
                         MeterRegistry meterRegistry) {
        this.reviewRepository = reviewRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.userService = userService;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public ReviewResponseDTO saveReview(Long motorcycleId, ReviewRequestDTO reviewRequestDTO, Authentication auth) {
        User user = userService.findByEmail(auth.getName());

        Optional<Review> existingReview = reviewRepository.findByMotorcycleIdAndUserId(motorcycleId, user.getId());
        if (existingReview.isPresent()) {
            meterRegistry.counter("app_review_actions_total", "action", "create", "result", "failure").increment();
            throw new IllegalArgumentException("One review is allowed per user");
        }
        Review review = ReviewMapper.toEntity(reviewRequestDTO);
        Motorcycle motorcycle = motorcycleRepository.findById(motorcycleId).orElseThrow();

        review.setMotorcycle(motorcycle);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);
        meterRegistry.counter("app_review_actions_total", "action", "create", "result", "success").increment();
        meterRegistry.counter(
                "moto_interest_manufacturer_total",
                "manufacturer", motorcycle.getManufacturer().name(),
                "source", "review_create"
        ).increment();
        meterRegistry.counter(
                "moto_interest_category_total",
                "category", motorcycle.getCategory().name(),
                "source", "review_create"
        ).increment();
        return ReviewMapper.toResponseDTO(savedReview);
    }

    @Transactional
    public ReviewResponseDTO updateReview(Long motorcycleId, ReviewRequestDTO reviewRequestDTO, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        Review review = reviewRepository.findByMotorcycleIdAndUserId(motorcycleId, user.getId())
                .orElseThrow(() -> {
                    meterRegistry.counter("app_review_actions_total", "action", "update", "result", "failure").increment();
                    return new IllegalArgumentException("There is no review for this user and motorcycle");
                });

        review.setRating(reviewRequestDTO.getRating());
        review.setComment(reviewRequestDTO.getComment());

        Review updatedReview = reviewRepository.save(review);
        meterRegistry.counter("app_review_actions_total", "action", "update", "result", "success").increment();
        return ReviewMapper.toResponseDTO(updatedReview);
    }

    @Transactional
    public void deleteReview(Long motorcycleId, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        Review review = reviewRepository.findByMotorcycleIdAndUserId(motorcycleId, user.getId())
                .orElseThrow(() -> {
                    meterRegistry.counter("app_review_actions_total", "action", "delete", "result", "failure").increment();
                    return new RuntimeException("Review not found for this motorcycle and user");
                });
        reviewRepository.delete(review);
        meterRegistry.counter("app_review_actions_total", "action", "delete", "result", "success").increment();
    }

    public List<ReviewResponseDTO> getMotorcycleReviews(Long motorcycleId) {
        List<Review> reviews = reviewRepository.findAllByMotorcycleId(motorcycleId);
        return reviews.stream().map(ReviewMapper::toResponseDTO).toList();
    }

    public List<ReviewResponseDTO> getReviewsByUserId(UUID userId) {
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream().map(ReviewMapper::toResponseDTO).toList();
    }
}
