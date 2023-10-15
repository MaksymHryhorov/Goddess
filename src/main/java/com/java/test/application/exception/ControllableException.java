package com.java.test.application.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ControllableException extends RuntimeException {
    private final int errorCode;
    private final String timestamp;

    public ControllableException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now().toString();
    }

}
