package com.vaiak.moto_compare.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaiak.moto_compare.dto.motorcycle.IncorrectSpecReportDTO;
import com.vaiak.moto_compare.dto.motorcycle.SubmitMotorcycleRequestDTO;
import com.vaiak.moto_compare.models.UserRequest;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SubmitRequestMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static UserRequest toUserRequest(@Valid SubmitMotorcycleRequestDTO motorcycleRequest) {
        Map<String, String> requestContent = new HashMap<>();
        requestContent.put("manufacturer", motorcycleRequest.getManufacturer().toString());
        requestContent.put("model", motorcycleRequest.getModel());
        requestContent.put("yearRange", motorcycleRequest.getYearRange());

        String jsonRequestContent;
        try {
            jsonRequestContent = objectMapper.writeValueAsString(requestContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting request content to JSON", e);
        }

        return UserRequest.builder()
                .newMotorcycleRequest(true)
                .requestContent(jsonRequestContent)
                .build();
    }

    public static UserRequest toUserRequest(IncorrectSpecReportDTO requestContent) {
        String jsonRequestContent;
        try {
            jsonRequestContent = objectMapper.writeValueAsString(requestContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting request content to JSON", e);
        }

        return UserRequest.builder()
                .newMotorcycleRequest(false)
                .requestContent(jsonRequestContent)
                .build();
    }
}
