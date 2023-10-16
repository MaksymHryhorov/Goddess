package com.java.test.application.controller;

import com.java.test.application.dto.RegistrationRequest;
import com.java.test.application.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@RequestMapping("/api")
@AllArgsConstructor
@Controller
public class RegistrationController {
    private UserServiceImpl userService;

    @PostMapping("/registration")
    public ResponseEntity<String> registerUser(@RequestBody final RegistrationRequest request) {
        String hashCode = userService.registerUser(request.getEmail(), request.getPassword());
        URI uri = userService.generateUserURI(hashCode);
        return ResponseEntity.created(uri).body(hashCode);
    }

    @PutMapping("/registration/{uuid}")
    public ResponseEntity<String> confirmRegistration(@RequestParam final Long token,
                                                      @PathVariable(name = "uuid") final String uuid) {
        userService.confirmRegistration(token, uuid);
        return ResponseEntity.ok("Email has been confirmed");
    }

}
