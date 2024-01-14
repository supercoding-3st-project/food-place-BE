package com.github.accountmanagementproject.service.customExceptions;

import lombok.Getter;

@Getter
public class DuplicateKeyException extends RuntimeException{
    private final String request;

    public DuplicateKeyException(String message, String request) {
        super(message);
        this.request = request;
    }
}
