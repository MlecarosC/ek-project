package com.eureka.tarea1_api.exception;

import java.time.LocalDate;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> validationErrors;

    public ValidationErrorResponse(LocalDate timestamp, int code, String message, String path, Map<String, String> validationErrors) {
        super(timestamp, code, message, path);
        this.validationErrors = validationErrors;
    }
}
