package com.vaiak.moto_compare.dto.manufacturer;

import com.vaiak.moto_compare.enums.Manufacturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularManufacturerDTO {

    private Manufacturer manufacturer;
    private Long count;
}
