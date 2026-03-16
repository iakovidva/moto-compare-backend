package com.vaiak.moto_compare.utils;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;

public class MotorcycleTestDataFactory {

    public static MotorcycleSummaryDTO createAdventureDTO(Long id, Manufacturer manufacturer, String yearRange) {
        return MotorcycleSummaryDTO.builder()
                .id(id)
                .manufacturer(manufacturer)
                .model("a model")
                .yearRange(yearRange)
                .image("image.jpg")
                .category(Category.ADVENTURE)
                .displacement(750)
                .horsePower(72)
                .build();
    }

    public static MotorcycleSummaryDTO createNakedDTO(Long id, Manufacturer manufacturer, String yearRange) {
        return MotorcycleSummaryDTO.builder()
                .id(id)
                .manufacturer(manufacturer)
                .model("a model")
                .yearRange(yearRange)
                .image("image.jpg")
                .category(Category.NAKED)
                .displacement(750)
                .horsePower(72)
                .build();
    }

    public static MotorcycleDetailsDTO createDetailsDTO(Long id, Manufacturer manufacturer, Category category) {
        MotorcycleDetailsDTO dto = new MotorcycleDetailsDTO();
        dto.setId(id);
        dto.setManufacturer(manufacturer);
        dto.setModel("A model");
        dto.setYearRange("2021-2023");
        dto.setImage("image.jpg");
        dto.setCategory(category);
        dto.setEngineDesign("Inline-4");
        dto.setDisplacement(649);
        dto.setHorsePower(95);
        dto.setTorque(64);
        dto.setWeight(202);
        dto.setTankCapacity(15);
        dto.setFrontWheelSize(17);
        dto.setRearWheelSize(17);
        return dto;
    }

}
