package com.github.foodplacebe.service.exceptions;

import com.github.foodplacebe.web.dto.account.AccountDto;
import lombok.Getter;

@Getter
public class NotFoundSocialAccount extends RuntimeException{
    private AccountDto request;
    private String detailMessage;


    public NotFoundSocialAccount(AccountDto request, String detailMessage) {
        this.request = request;
        this.detailMessage = detailMessage;
    }
}
