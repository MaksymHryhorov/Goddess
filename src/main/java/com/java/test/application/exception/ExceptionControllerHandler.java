package com.java.test.application.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.java.test.application.utils.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class ExceptionControllerHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return getValidationErrors(exception.getBindingResult().getAllErrors(), ErrorCode.BAD_REQUEST.getCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolationExceptions(final ConstraintViolationException exception) {
        return getValidationErrors(exception.getConstraintViolations(), ErrorCode.BAD_REQUEST.getCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public Map<String, String> handleInvalidFormatExceptions(final InvalidFormatException exception) {
        return getValidationErrors(exception, ErrorCode.BAD_REQUEST.getCode());
    }

    private Map<String, String> getValidationErrors(final InvalidFormatException exception, final int errorCode) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getPath().forEach(error -> {
            String fieldName = error.getFieldName();
            String errorMessage = ErrorCode.BAD_REQUEST.getMessage() + fieldName;
            populateErrorMap(errorMap, errorCode, fieldName, errorMessage);
        });
        return errorMap;
    }

    private Map<String, String> getValidationErrors(final Iterable<? extends ObjectError> errors, final int errorCode) {
        Map<String, String> errorMap = new HashMap<>();
        errors.forEach(error -> {
            String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            populateErrorMap(errorMap, errorCode, fieldName, errorMessage);
        });
        return errorMap;
    }

    private Map<String, String> getValidationErrors(final Set<ConstraintViolation<?>> violations, final int errorCode) {
        Map<String, String> errorMap = new HashMap<>();
        violations.forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            populateErrorMap(errorMap, errorCode, fieldName, errorMessage);
        });
        return errorMap;
    }

    private void populateErrorMap(final Map<String, String> errorMap, final int errorCode,
                                  final String fieldName, final String errorMessage) {
        errorMap.put("code", String.valueOf(errorCode));
        errorMap.put(fieldName, errorMessage);
    }
}


