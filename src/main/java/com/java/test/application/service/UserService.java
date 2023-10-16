package com.java.test.application.service;

import java.net.URI;

public interface UserService {
    String registerUser(String email, String password);

    void confirmRegistration(final Long providedToken, final String providedUuid);

    URI generateUserURI(final String token);
}
