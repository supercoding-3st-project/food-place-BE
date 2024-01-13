package com.example.foodplacebe.service.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    private String detailMessage;
    private String request;

    public BadRequestException(String detailMessage, String request) {
        this.detailMessage = detailMessage;
        this.request = request;
    }

}
