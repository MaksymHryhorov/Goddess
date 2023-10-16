package com.java.test.application.dto;

import lombok.Data;

import javax.validation.Valid;

@Data
@Valid
public class LoginRequest {
    private String email;
    private String password;

}
