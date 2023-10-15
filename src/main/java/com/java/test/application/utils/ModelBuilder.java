package com.java.test.application.utils;

import com.java.test.application.model.User;
import org.springframework.stereotype.Component;

@Component
public class ModelBuilder {
    public static User buildUser(String email, String password) {
        return User.builder()
                .roleId(3L)
                .email(email)
                .password(password)
                .confirmed(false)
                .build();
    }
}
