package com.java.test.application.controller;

import com.java.test.application.dto.LoginRequest;
import com.java.test.application.service.impl.CustomUserDetailsService;
import com.java.test.application.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/login")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody @Valid final LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String token = JwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}
