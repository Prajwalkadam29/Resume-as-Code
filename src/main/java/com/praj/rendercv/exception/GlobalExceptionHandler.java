package com.praj.rendercv.exception;

import com.praj.rendercv.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(YamlParsingException.class)
    public ResponseEntity<ApiError> handleYamlParsing(YamlParsingException e) {
        ApiError error = ApiError.builder()
                .message(e.getMessage())
                .code("INVALID_YAML_CONTENT")
                .timestamp(LocalDateTime.now())
                .errors(e.getValidationErrors())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception e) {
        // Log the actual stack trace for the developer, but don't send it to the user
        log.error("Unhandled exception: ", e);

        ApiError error = ApiError.builder()
                .message("An unexpected internal error occurred.")
                .code("INTERNAL_SERVER_ERROR")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
