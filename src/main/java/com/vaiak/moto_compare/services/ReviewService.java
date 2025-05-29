package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.mappers.ReviewMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.Review;
import com.vaiak.moto_compare.repositories.MotorcycleRepository;
import com.vaiak.moto_compare.repositories.ReviewRepository;
import com.vaiak.moto_compare.security.models.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MotorcycleRepository motorcycleRepository;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository,
                         MotorcycleRepository motorcycleRepository,
                         UserService userService) {
        this.reviewRepository = reviewRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.userService = userService;
    }

    @Transactional
    public ReviewResponseDTO saveReview(Long motorcycleId, ReviewRequestDTO reviewRequestDTO, Authentication auth) {
        User user = userService.findByEmail(auth.getName());

        Review review = ReviewMapper.toEntity(reviewRequestDTO);
        Motorcycle motorcycle = motorcycleRepository.findById(motorcycleId).orElseThrow();

        review.setMotorcycle(motorcycle);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);
        return ReviewMapper.toResponseDTO(savedReview);
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
