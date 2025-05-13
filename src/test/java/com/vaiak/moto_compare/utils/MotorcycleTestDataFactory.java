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
        return MotorcycleDetailsDTO.builder()
                .id(id)
                .manufacturer(manufacturer)
                .model("A model")
                .yearRange("2021-2023")
                .image("image.jpg")
                .category(category)
                .engineDesign("Inline-4")
                .displacement(649)
                .horsePower(95)
                .torque(64)
                .weight(202)
                .tankCapacity(15)
                .frontWheelSize(17)
                .rearWheelSize(17)
                .build();
    }

}
