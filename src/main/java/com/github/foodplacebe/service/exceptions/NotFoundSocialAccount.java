package com.github.foodplacebe.service.exceptions;

import com.github.foodplacebe.web.dto.account.SocialAccountDto;
import lombok.Getter;

@Getter
public class NotFoundSocialAccount extends RuntimeException{
    private SocialAccountDto request;
    private String detailMessage;


    public NotFoundSocialAccount(SocialAccountDto request, String detailMessage) {
        this.request = request;
        this.detailMessage = detailMessage;
    }
}
