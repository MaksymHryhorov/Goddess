package com.java.test.application.service.impl;

import com.java.test.application.exception.ControllableException;
import com.java.test.application.model.OneTimeToken;
import com.java.test.application.model.User;
import com.java.test.application.repository.OneTimeTokenRepository;
import com.java.test.application.repository.UserRepository;
import com.java.test.application.service.UserService;
import com.java.test.application.utils.ErrorCode;
import com.java.test.application.utils.ModelBuilder;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OneTimeTokenRepository oneTimeTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(String email, String password) {
        checkUserExistence(email);
        String encodedPassword = passwordEncoder.encode(password);
        User user = ModelBuilder.buildUser(email, encodedPassword);
        userRepository.save(user);
        return generateAndSaveOneTimeToken(user.getId()).getUuid();
    }

    @Transactional
    public void confirmRegistration(Long providedToken, String providedUuid) {
        User user = findUserByOneTimeTokenUuid(providedUuid);
        if (!verifyOneTimeToken(providedToken, providedUuid)) {
            throw new ControllableException(getExceptionErrorCode(), "Email confirmation token is wrong");
        }
        deleteOneTimeTokenByUuid(providedUuid);
        user.setConfirmed(true);
        userRepository.save(user);
    }

    public URI generateUserURI(String token) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(token)
                .toUri();
    }

    private void checkUserExistence(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ControllableException(getExceptionErrorCode(), "User with this email already exists");
        }
    }

    private OneTimeToken generateAndSaveOneTimeToken(Long userId) {
        OneTimeToken token = new OneTimeToken();
        token.setUserId(userId);
        token.setUuid(generateConfirmationCode());
        token.setToken(generateOneTimeToken());
        token.setExpirationDate(LocalDate.now().plusDays(1));
        oneTimeTokenRepository.save(token);
        return token;
    }

    private String generateConfirmationCode() {
        return UUID.randomUUID().toString();
    }

    private Long generateOneTimeToken() {
        Random random = new Random();
        return (long) (100000 + random.nextInt(900000));
    }

    private boolean verifyOneTimeToken(Long providedToken, String providedUuid) {
        return oneTimeTokenRepository.findTokenByUuid(providedUuid)
                .map(OneTimeToken::getToken)
                .map(token -> token.equals(providedToken))
                .orElseThrow(() -> new ControllableException(getExceptionErrorCode(),
                        "Token does not match or is not found for the provided UUID."));
    }

    private User findUserByOneTimeTokenUuid(String uuid) {
        long userId = oneTimeTokenRepository.findTokenByUuid(uuid)
                .map(OneTimeToken::getUserId)
                .orElseThrow(() -> new ControllableException(getExceptionErrorCode(),
                        "User id " + uuid + " not found"));
        return findUserById(userId);
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ControllableException(getExceptionErrorCode(),
                        "User with id " + userId + " not found"));
    }

    private void deleteOneTimeTokenByUuid(String providedUuid) {
        oneTimeTokenRepository.deleteOneTimeTokenByUuid(providedUuid);
    }

    private int getExceptionErrorCode() {
        return ErrorCode.USER_REGISTRATION_EXCEPTION.getCode();
    }
}
