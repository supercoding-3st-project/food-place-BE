package com.github.foodplacebe.service.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    private String detailMessage;
    private Object request;

    public BadRequestException(String detailMessage, Object request) {
        this.detailMessage = detailMessage;
        this.request = request;
    }

}
