package com.vaiak.moto_compare.mappers;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.models.Review;

public class ReviewMapper {

    public static ReviewResponseDTO toResponseDTO(Review review) {
        return ReviewResponseDTO.builder()
                .userName(review.getUser().getUserName())
                .motorcycleId(review.getMotorcycle().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static Review toEntity(ReviewRequestDTO reviewRequestDTO) {
                return Review.builder()
                        .rating(reviewRequestDTO.getRating())
                        .comment(reviewRequestDTO.getComment())
                        .build();

    }
}
