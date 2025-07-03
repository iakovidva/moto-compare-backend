package com.vaiak.moto_compare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitFeedbackRequestDTO {

    @NotNull(message = "type is a required field")
    private String type;

    private String subject;

    @NotBlank(message = "text is a required field")
    private String text;
}
