package com.vaiak.moto_compare.dto.motorcycle;

import com.vaiak.moto_compare.enums.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MotorcycleSearchParams {
    private int page = 0;
    private int size = 3;
    private Category category;
    private String manufacturer; //TODO consider switching to ENUM
    private Integer horsePowerMin;
    private Integer horsePowerMax;
    private Integer displacementMin;
    private Integer displacementMax;
    private Integer yearMin;
    private Integer yearMax;
    private String search;
    private String sort;
}
