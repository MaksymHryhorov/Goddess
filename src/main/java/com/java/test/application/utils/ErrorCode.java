package com.java.test.application.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    SERVER_ERROR(5000,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Sorry, the server is currently unavailable."),
    BAD_REQUEST(4000,
            HttpStatus.BAD_REQUEST,
            "Invalid format for field: "),
    UNAUTHORIZED(4001,
            HttpStatus.UNAUTHORIZED,
            "You are not authorized to execute this request."),
    FORBIDDEN(4003,
            HttpStatus.FORBIDDEN,
            "You don't have access to execute this request."),
    NOT_FOUND(4004,
            HttpStatus.NOT_FOUND,
            "Sorry, but the page or resource you're looking for cannot be found."),
    METHOD_NOT_ALLOWED(4005,
            HttpStatus.METHOD_NOT_ALLOWED,
            "Method not allowed.");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(final int code,
              final HttpStatus httpStatus,
              final String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
