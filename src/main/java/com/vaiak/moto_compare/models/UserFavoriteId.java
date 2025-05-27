package com.vaiak.moto_compare.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteId {
    private UUID userId;
    private Long motorcycleId;
}
