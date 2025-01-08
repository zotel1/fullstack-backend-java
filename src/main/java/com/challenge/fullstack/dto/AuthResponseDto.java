package com.challenge.fullstack.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    String token;
    String refreshToken;
}
