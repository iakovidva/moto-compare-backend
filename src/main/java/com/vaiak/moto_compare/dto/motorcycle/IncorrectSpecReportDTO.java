package com.vaiak.moto_compare.dto.motorcycle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncorrectSpecReportDTO {
    private Long motorcycleId;
    private List<IncorrectField> data;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class IncorrectField {
    private String field;
    private String oldValue;
    private String newValue;
}
