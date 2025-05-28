package com.vaiak.moto_compare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserDTO {

    private String userName;
    private String email;
    private LocalDateTime createdAt;
}
