package com.java.test.application.dto;

import lombok.Data;

import javax.validation.Valid;

@Data
@Valid
public class RegistrationRequest {
    private String email;
    private String password;

}
