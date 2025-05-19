package com.vaiak.moto_compare.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {
    
    private String accessToken;
}
