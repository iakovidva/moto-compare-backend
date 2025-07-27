package com.vaiak.moto_compare.dto.motorcycle;

import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankedMotorcycleDTO extends MotorcycleSummaryDTO {

    private double score;
    private int weight;
    private double fuelConsumption;

    public RankedMotorcycleDTO(
            Long id,
            Manufacturer manufacturer,
            String model,
            String yearRange,
            String image,
            Category category,
            int displacement,
            int horsePower,
            double score,
            int weight,
            double fuelConsumption
    ) {
        super(id, manufacturer, model, yearRange, image, category, displacement, horsePower);
        this.score = score;
        this.weight = weight;
        this.fuelConsumption = fuelConsumption;
    }

    public static RankedMotorcycleDTO of(MotorcycleSummaryDTO base, int weight, double fuelConsumption, double score) {
        return new RankedMotorcycleDTO(
                base.getId(),
                base.getManufacturer(),
                base.getModel(),
                base.getYearRange(),
                base.getImage(),
                base.getCategory(),
                base.getDisplacement(),
                base.getHorsePower(),
                score,
                weight,
                fuelConsumption
        );
    }
}
