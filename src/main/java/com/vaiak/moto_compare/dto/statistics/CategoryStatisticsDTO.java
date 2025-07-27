package com.vaiak.moto_compare.dto.statistics;

import com.vaiak.moto_compare.enums.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryStatisticsDTO {
    private Category category;
    private Long counter;
}
