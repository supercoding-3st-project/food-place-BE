package com.github.accountmanagementproject.service.customExceptions;

import lombok.Getter;

@Getter
public class AccessDenied extends RuntimeException{
    private final String request;

    public AccessDenied(String message, String request) {
        super(message);
        this.request = request;
    }
}
