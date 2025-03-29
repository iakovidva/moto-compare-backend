package com.vaiak.moto_compare.dto.motorcycle;

import com.vaiak.moto_compare.enums.Manufacturer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitMotorcycleRequestDTO {

    @NotNull(message = "manufacturer is a required field")
    private Manufacturer manufacturer;

    @NotBlank(message = "model is a required field")
    private String model;

    @NotBlank(message = "yearRange is a required field")
    private String yearRange;
}
