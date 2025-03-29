package com.vaiak.moto_compare.dto.motorcycle;

import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MotorcycleSummaryDTO {

    private Long id;

    @NotNull(message = "Manufacturer cannot be null")
    private Manufacturer manufacturer;

    @NotNull(message = "Model cannot be null")
    private String model;

    @NotNull(message = "Year range cannot be null")
    private String yearRange;

    @NotNull(message = "Image cannot be null")
    private String image;

    @NotNull
    private Category category;

    @NotNull
    private int displacement;

    @NotNull
    private int horsePower;
}
