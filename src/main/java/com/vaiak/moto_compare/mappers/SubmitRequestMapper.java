package com.vaiak.moto_compare.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaiak.moto_compare.dto.motorcycle.IncorrectSpecReportDTO;
import com.vaiak.moto_compare.dto.motorcycle.SubmitMotorcycleRequestDTO;
import com.vaiak.moto_compare.enums.Status;
import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.security.models.User;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

public class SubmitRequestMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static UserRequest toUserRequest(@Valid SubmitMotorcycleRequestDTO motorcycleRequest, User user) {
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
                .status(Status.SUBMITTED)
                .user(user)
                .build();
    }

    public static UserRequest toUserRequest(IncorrectSpecReportDTO requestContent, User user) {
        String jsonRequestContent;
        try {
            jsonRequestContent = objectMapper.writeValueAsString(requestContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting request content to JSON", e);
        }

        return UserRequest.builder()
                .newMotorcycleRequest(false)
                .requestContent(jsonRequestContent)
                .status(Status.SUBMITTED)
                .user(user)
                .build();
    }
}
