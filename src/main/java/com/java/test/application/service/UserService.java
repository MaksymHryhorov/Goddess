package com.java.test.application.service;

import com.java.test.application.exception.ControllableException;
import com.java.test.application.model.OneTimeToken;
import com.java.test.application.model.User;
import com.java.test.application.repository.OneTimeTokenRepository;
import com.java.test.application.repository.UserRepository;
import com.java.test.application.utils.ErrorCode;
import com.java.test.application.utils.ModelBuilder;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;
    private OneTimeTokenRepository oneTimeTokenRepository;

    public String registerUser(final String email, final String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ControllableException(getExceptionErrorCode(), "User with this email already existed");
        }
        String encodedPassword = encodeUserPassword(password);
        User user = ModelBuilder.buildUser(email, encodedPassword);
        userRepository.save(user);
        return generateAndSaveOneTimeToken(user.getId()).getUuid();
    }


    @Transactional
    public void confirmRegistration(final Long providedToken, final String providedUuid) {
        User user = findUserByOneTimeTokenUuid(providedUuid);
        if (verifyOneTimeToken(providedToken, providedUuid)) {
            deleteOneTimeTokenByUuid(providedUuid);
            user.setConfirmed(true);
            userRepository.save(user);
        }
        throw new ControllableException(getExceptionErrorCode(), "Email confirmation token is wrong");
    }

    private OneTimeToken generateAndSaveOneTimeToken(final Long userId) {
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

    private boolean verifyOneTimeToken(final Long providedToken, final String providedUuid) {
        return oneTimeTokenRepository.findTokenByUuid(providedUuid)
                .map(OneTimeToken::getToken)
                .map(token -> token.equals(providedToken))
                .orElseThrow(() -> new ControllableException(getExceptionErrorCode(),
                        "Token does not match or is not found for the provided UUID."));
    }

    private User findUserByOneTimeTokenUuid(final String uuid) {
        long userId = oneTimeTokenRepository.findTokenByUuid(uuid)
                .map(OneTimeToken::getUserId)
                .orElseThrow(() -> new ControllableException(getExceptionErrorCode(),
                        "User id " + uuid + " not found"));
        return findUserById(userId);
    }

    private User findUserById(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ControllableException(getExceptionErrorCode(),
                        "User with id" + userId + " not found"));
    }

    private void deleteOneTimeTokenByUuid(final String providedUuid) {
        oneTimeTokenRepository.deleteOneTimeTokenByUuid(providedUuid);
    }

    private String encodeUserPassword(final String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
        return bCryptPasswordEncoder.encode(password);
    }

    private int getExceptionErrorCode() {
        return ErrorCode.USER_REGISTRATION_EXCEPTION.getCode();
    }
}
