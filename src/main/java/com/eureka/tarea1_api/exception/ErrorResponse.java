package com.eureka.tarea1_api.exception;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDate timestamp;
    private int code;
    private String message;
    private String path;
}
