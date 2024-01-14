package com.github.accountmanagementproject.service.customExceptions;

import lombok.Getter;

@Getter
public class CustomBadCredentialsException extends RuntimeException{
    private final String request;

    public CustomBadCredentialsException(String message, String request) {
        super(message);
        this.request = request;
    }
}
