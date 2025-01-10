package com.challenge.fullstack.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String user;
    private String password;
}
