package com.vaiak.moto_compare.mappers;

import com.vaiak.moto_compare.dto.review.ReviewRequestDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.Review;

public class ReviewMapper {

    public static ReviewResponseDTO toResponseDTO(Review review) {
        Motorcycle motorcycle = review.getMotorcycle();
        return ReviewResponseDTO.builder()
                .userName(review.getUser().getUserName())
                .motorcycleId(motorcycle.getId())
                .motorcycleName(motorcycle.getManufacturer().name() + " " + motorcycle.getModel() + " " + motorcycle.getYearRange())
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
