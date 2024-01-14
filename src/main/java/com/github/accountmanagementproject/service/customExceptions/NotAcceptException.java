package com.github.accountmanagementproject.service.customExceptions;

import lombok.Getter;

@Getter
public class NotAcceptException extends RuntimeException{
    private final String request;

    public NotAcceptException(String message, String request) {
        super(message);
        this.request = request;
    }
}
