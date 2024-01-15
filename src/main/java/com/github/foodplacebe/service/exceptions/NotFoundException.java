package com.github.foodplacebe.service.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private String detailMessage;
    private String request;

    public NotFoundException(String detailMessage, String request) {
        this.detailMessage = detailMessage;
        this.request = request;
    }
}
