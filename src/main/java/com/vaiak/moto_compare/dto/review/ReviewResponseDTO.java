package com.vaiak.moto_compare.dto.review;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponseDTO {
    private String userName;
    private Long motorcycleId;
    private String motorcycleName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}