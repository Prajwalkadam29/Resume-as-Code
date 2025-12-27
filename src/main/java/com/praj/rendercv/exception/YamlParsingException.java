package com.praj.rendercv.exception;

import com.praj.rendercv.dto.ApiError;
import lombok.Getter;
import java.util.List;

@Getter
public class YamlParsingException extends RuntimeException {
    private final List<ApiError.ValidationError> validationErrors;

    // For general YAML syntax errors
    public YamlParsingException(String message, Throwable cause) {
        super(message, cause);
        this.validationErrors = null;
    }

    // For structured validation errors (Bean Validation)
    public YamlParsingException(String message, List<ApiError.ValidationError> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }
}
