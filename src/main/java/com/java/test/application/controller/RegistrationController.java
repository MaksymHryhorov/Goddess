package com.java.test.application.controller;

import com.java.test.application.dto.RegistrationRequest;
import com.java.test.application.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api")
@AllArgsConstructor
@Controller
public class RegistrationController {
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> registerUser(@RequestBody final RegistrationRequest request) {
        String token = userService.registerUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registration/{uuid}")
    public ResponseEntity<String> confirmRegistration(@RequestParam final Long token,
                                                      @PathVariable(name = "uuid") final String uuid) {
        userService.confirmRegistration(token, uuid);
        return ResponseEntity.ok("Registration confirmed");
    }

}
