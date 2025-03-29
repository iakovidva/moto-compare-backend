package com.vaiak.moto_compare.mappers;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.models.Motorcycle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MotorcycleMapper {

    public static MotorcycleDetailsDTO toDetailsDTO(Motorcycle motorcycle) {
        if (motorcycle == null) {
            return null;
        }

        List<ReviewResponseDTO> reviews = motorcycle.getReviews().stream().map(ReviewMapper::toResponseDTO).toList();
        double averageRating = reviews.stream().mapToInt(ReviewResponseDTO::getRating).average().orElse(0);
        double roundedRating = BigDecimal.valueOf(averageRating)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        return MotorcycleDetailsDTO.builder()
                .manufacturer(motorcycle.getManufacturer())
                .model(motorcycle.getModel())
                .yearRange(motorcycle.getYearRange())
                .image(motorcycle.getImage())
                .category(motorcycle.getCategory())
                // Engine fields
                .engineDesign(motorcycle.getEngineDesign())
                .displacement(motorcycle.getDisplacement())
                .horsePower(motorcycle.getHorsePower())
                .torque(motorcycle.getTorque())
                .bore(motorcycle.getBore())
                .stroke(motorcycle.getStroke())
                .compressionRatio(motorcycle.getCompressionRatio())
                .coolingSystem(motorcycle.getCoolingSystem())
                .fuelConsumption(motorcycle.getFuelConsumption())
                .emissions(motorcycle.getEmissions())
                .fuelSystem(motorcycle.getFuelSystem())
                .throttleControl(motorcycle.getThrottleControl())
                .clutch(motorcycle.getClutch())
                .transmission(motorcycle.getTransmission())
                // Chassis
                .weight(motorcycle.getWeight())
                .tankCapacity(motorcycle.getTankCapacity())
                .wheelbase(motorcycle.getWheelbase())
                .groundClearance(motorcycle.getGroundClearance())
                .frameDesign(motorcycle.getFrameDesign())
                .frontSuspension(motorcycle.getFrontSuspension())
                .rearSuspension(motorcycle.getRearSuspension())
                .frontSuspensionTravel(motorcycle.getFrontSuspensionTravel())
                .rearSuspensionTravel(motorcycle.getRearSuspensionTravel())
                .seatHeight(motorcycle.getSeatHeight())
                // Wheels
                .wheelsType(motorcycle.getWheelsType())
                .frontTyre(motorcycle.getFrontTyre())
                .rearTyre(motorcycle.getRearTyre())
                .frontWheelSize(motorcycle.getFrontWheelSize())
                .rearWheelSize(motorcycle.getRearWheelSize())
                // Brakes
                .frontBrake(motorcycle.getFrontBrake())
                .rearBrake(motorcycle.getRearBrake())
                .frontDiscDiameter(motorcycle.getFrontDiscDiameter())
                .rearDiscDiameter(motorcycle.getRearDiscDiameter())
                .abs(motorcycle.isAbs())
                // Electronics
                .dashDisplay(motorcycle.getDashDisplay())
                .ridingModes(motorcycle.getRidingModes())
                .tractionControl(motorcycle.isTractionControl())
                .quickShifter(motorcycle.getQuickShifter())
                .cruiseControl(motorcycle.isCruiseControl())
                .lighting(motorcycle.getLighting())
                .similarMotorcycles(getSimilarMotorcycles(motorcycle))
                .reviews(reviews)
                .averageRating(roundedRating)
                .numberOfReviews(reviews.size())
                .build();
    }

    private static Set<MotorcycleSummaryDTO> getSimilarMotorcycles(Motorcycle motorcycle) {
        return motorcycle.getSimilarMotorcycles().stream()
                .map(MotorcycleMapper::toSummaryDTO)
                .collect(Collectors.toSet());
    }

    public static MotorcycleSummaryDTO toSummaryDTO(Motorcycle m) {
        return MotorcycleSummaryDTO.builder()
                .id(m.getId())
                .manufacturer(m.getManufacturer())
                .model(m.getModel())
                .yearRange(m.getYearRange())
                .image(m.getImage())
                .displacement(m.getDisplacement())
                .horsePower(m.getHorsePower())
                .build();
    }

    public static Motorcycle toEntity(MotorcycleDetailsDTO dto) {
        if (dto == null) {
            return null;
        }

        return Motorcycle.builder()
                .manufacturer(dto.getManufacturer())
                .model(dto.getModel())
                .yearRange(dto.getYearRange())
                .image(dto.getImage())
                .category(dto.getCategory())
                // Engine
                .engineDesign(dto.getEngineDesign())
                .displacement(dto.getDisplacement())
                .horsePower(dto.getHorsePower())
                .torque(dto.getTorque())
                .bore(dto.getBore())
                .stroke(dto.getStroke())
                .compressionRatio(dto.getCompressionRatio())
                .coolingSystem(dto.getCoolingSystem())
                .fuelConsumption(dto.getFuelConsumption())
                .emissions(dto.getEmissions())
                .fuelSystem(dto.getFuelSystem())
                .throttleControl(dto.getThrottleControl())
                .clutch(dto.getClutch())
                .transmission(dto.getTransmission())
                // Chassis
                .weight(dto.getWeight())
                .tankCapacity(dto.getTankCapacity())
                .wheelbase(dto.getWheelbase())
                .groundClearance(dto.getGroundClearance())
                .frameDesign(dto.getFrameDesign())
                .frontSuspension(dto.getFrontSuspension())
                .rearSuspension(dto.getRearSuspension())
                .frontSuspensionTravel(dto.getFrontSuspensionTravel())
                .rearSuspensionTravel(dto.getRearSuspensionTravel())
                .seatHeight(dto.getSeatHeight())
                // Wheels
                .wheelsType(dto.getWheelsType())
                .frontTyre(dto.getFrontTyre())
                .rearTyre(dto.getRearTyre())
                .frontWheelSize(dto.getFrontWheelSize())
                .rearWheelSize(dto.getRearWheelSize())
                // Brakes
                .frontBrake(dto.getFrontBrake())
                .rearBrake(dto.getRearBrake())
                .frontDiscDiameter(dto.getFrontDiscDiameter())
                .rearDiscDiameter(dto.getRearDiscDiameter())
                .abs(dto.isAbs())
                // Electronics
                .dashDisplay(dto.getDashDisplay())
                .ridingModes(dto.getRidingModes())
                .tractionControl(dto.isTractionControl())
                .quickShifter(dto.getQuickShifter())
                .cruiseControl(dto.isCruiseControl())
                .lighting(dto.getLighting())
                // We ignore similarMotorcycles here because it will be filled later
                .similarMotorcycles(new HashSet<>())
                .build();
    }
}
