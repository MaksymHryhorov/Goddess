package com.java.test.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@Valid
public class RegistrationRequest {
    private String email;
    private String password;

}
