package com.java.test.application.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.java.test.application.utils.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
    public Map<String, String> handleValidationExceptions(final MethodArgumentNotValidException exception) {
        return getValidationErrors(exception.getBindingResult().getAllErrors(), ErrorCode.BAD_REQUEST.getCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleValidationExceptions(final ConstraintViolationException exception) {
        return getValidationErrors(exception.getConstraintViolations(), ErrorCode.BAD_REQUEST.getCode());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleInvalidFormatException(final InvalidFormatException exception) {
        String fieldName = StringUtils.capitalize(exception.getPath().get(0).getFieldName());
        String message = "Invalid format for field: " + fieldName;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    private Map<String, String> getValidationErrors(final Iterable<? extends ObjectError> errors,
                                                    final int errorCode) {
        Map<String, String> errorMap = new HashMap<>();
        errors.forEach(error -> {
            String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errorMap.put("code", String.valueOf(errorCode));
            errorMap.put(fieldName, errorMessage);
        });
        return errorMap;
    }

    private Map<String, String> getValidationErrors(final Set<ConstraintViolation<?>> violations,
                                                    final int errorCode) {
        Map<String, String> errorMap = new HashMap<>();
        violations.forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errorMap.put("code", String.valueOf(errorCode));
            errorMap.put(fieldName, errorMessage);
        });
        return errorMap;
    }
}


