package com.vaiak.moto_compare.dto.statistics;

import com.vaiak.moto_compare.enums.Manufacturer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManufacturerStatisticsDTO {
    private Manufacturer manufacturer;
    private Long counter;
}
