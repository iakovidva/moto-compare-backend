package com.vaiak.moto_compare.mappers;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.models.Motorcycle;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MotorcycleMapper {

    public static MotorcycleDetailsDTO toDetailsDTO(Motorcycle motorcycle) {
        if (motorcycle == null) {
            return null;
        }

        MotorcycleDetailsDTO dto = new MotorcycleDetailsDTO();
        dto.setId(motorcycle.getId());
        dto.setManufacturer(motorcycle.getManufacturer());
        dto.setModel(motorcycle.getModel());
        dto.setYearRange(motorcycle.getYearRange());
        dto.setImage(motorcycle.getImage());
        dto.setCategory(motorcycle.getCategory());
        // Engine fields
        dto.setEngineDesign(motorcycle.getEngineDesign());
        dto.setDisplacement(motorcycle.getDisplacement());
        dto.setHorsePower(motorcycle.getHorsePower());
        dto.setTorque(motorcycle.getTorque());
        dto.setBore(motorcycle.getBore());
        dto.setStroke(motorcycle.getStroke());
        dto.setCompressionRatio(motorcycle.getCompressionRatio());
        dto.setCoolingSystem(motorcycle.getCoolingSystem());
        dto.setFuelConsumption(motorcycle.getFuelConsumption());
        dto.setEmissions(motorcycle.getEmissions());
        dto.setFuelSystem(motorcycle.getFuelSystem());
        dto.setThrottleControl(motorcycle.getThrottleControl());
        dto.setClutch(motorcycle.getClutch());
        dto.setTransmission(motorcycle.getTransmission());
        // Chassis
        dto.setWeight(motorcycle.getWeight());
        dto.setTankCapacity(motorcycle.getTankCapacity());
        dto.setWheelbase(motorcycle.getWheelbase());
        dto.setGroundClearance(motorcycle.getGroundClearance());
        dto.setFrameDesign(motorcycle.getFrameDesign());
        dto.setFrontSuspension(motorcycle.getFrontSuspension());
        dto.setRearSuspension(motorcycle.getRearSuspension());
        dto.setFrontSuspensionTravel(motorcycle.getFrontSuspensionTravel());
        dto.setRearSuspensionTravel(motorcycle.getRearSuspensionTravel());
        dto.setSeatHeight(motorcycle.getSeatHeight());
        // Wheels
        dto.setWheelsType(motorcycle.getWheelsType());
        dto.setFrontTyre(motorcycle.getFrontTyre());
        dto.setRearTyre(motorcycle.getRearTyre());
        dto.setFrontWheelSize(motorcycle.getFrontWheelSize());
        dto.setRearWheelSize(motorcycle.getRearWheelSize());
        // Brakes
        dto.setFrontBrake(motorcycle.getFrontBrake());
        dto.setRearBrake(motorcycle.getRearBrake());
        dto.setFrontDiscDiameter(motorcycle.getFrontDiscDiameter());
        dto.setRearDiscDiameter(motorcycle.getRearDiscDiameter());
        dto.setAbs(motorcycle.isAbs());
        // Electronics
        dto.setDashDisplay(motorcycle.getDashDisplay());
        dto.setRidingModes(motorcycle.getRidingModes());
        dto.setTractionControl(motorcycle.isTractionControl());
        dto.setQuickShifter(motorcycle.getQuickShifter());
        dto.setCruiseControl(motorcycle.isCruiseControl());
        dto.setLighting(motorcycle.getLighting());
        dto.setSimilarMotorcycles(getSimilarMotorcycles(motorcycle));
//                .reviews(reviews)
//                .averageRating(roundedRating)
//                .numberOfReviews(reviews.size())
        return dto;
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
