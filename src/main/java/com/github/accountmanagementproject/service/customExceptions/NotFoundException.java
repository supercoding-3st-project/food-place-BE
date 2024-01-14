package com.github.accountmanagementproject.service.customExceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private final String request;

    public NotFoundException(String message, String request) {
        super(message);
        this.request = request;
    }
}
