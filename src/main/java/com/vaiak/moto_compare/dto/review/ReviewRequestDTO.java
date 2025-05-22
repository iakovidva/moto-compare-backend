package com.vaiak.moto_compare.dto.review;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReviewRequestDTO {
    private int rating;
    private String comment;
}
