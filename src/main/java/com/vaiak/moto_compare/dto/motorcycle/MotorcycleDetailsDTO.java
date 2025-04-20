package com.vaiak.moto_compare.dto.motorcycle;

import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class MotorcycleDetailsDTO {

    @NotNull
    private Long id;

    @NotNull
    private Manufacturer manufacturer;

    @NotBlank
    private String model;

    @NotBlank
    private String yearRange;

    @NotBlank
    private String image;

    @NotNull
    private Category category;

    // Engine fields
    @NotBlank
    private String engineDesign;

    @Min(50)
    private int displacement;

    @Min(1)
    private int horsePower;

    @Min(1)
    private int torque;

    private int bore;
    private int stroke;
    private double compressionRatio;

    private String coolingSystem;

    @Min(0)
    private double fuelConsumption;

    private String emissions;
    private String fuelSystem;
    private String throttleControl;
    private String clutch;
    private String transmission;

    // Chassis
    @Min(1)
    private int weight;

    @Min(1)
    private int tankCapacity;

    private int wheelbase;
    private int groundClearance;
    private String frameDesign;
    private String frontSuspension;
    private String rearSuspension;
    private int frontSuspensionTravel;
    private int rearSuspensionTravel;
    private String seatHeight;

    // Wheels
    private String wheelsType;
    private String frontTyre;
    private String rearTyre;

    @Min(1)
    private int frontWheelSize;

    @Min(1)
    private int rearWheelSize;

    // Brakes
    private String frontBrake;
    private String rearBrake;
    private int frontDiscDiameter;
    private int rearDiscDiameter;
    private boolean abs;

    // Electronics
    private String dashDisplay;
    private String ridingModes;
    private boolean tractionControl;
    private String quickShifter;
    private boolean cruiseControl;
    private String lighting;

    private Set<MotorcycleSummaryDTO> similarMotorcycles;

    // Reviews
    private List<ReviewResponseDTO> reviews;
    private double averageRating;
    private int numberOfReviews;
}
