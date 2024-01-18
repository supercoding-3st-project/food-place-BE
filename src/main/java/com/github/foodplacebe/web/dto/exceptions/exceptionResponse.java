package com.github.foodplacebe.web.dto.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class exceptionResponse {
    private ErrorDetail error;

    public static class ErrorDetail {
        private int code;
        private HttpStatus httpStatus;
        private String message;
        private String request;
        private LocalDateTime timestamp;
    }
}
