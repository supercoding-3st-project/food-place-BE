package com.github.accountmanagementproject.service.customExceptions;


import lombok.Getter;

@Getter
public class CustomBindException extends RuntimeException{
    private final String request;

    public CustomBindException(String message, String request) {
        super(message);
        this.request = request;
    }

}
