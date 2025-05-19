package com.vaiak.moto_compare.security.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
