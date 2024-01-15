package com.github.foodplacebe.service.exceptions;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class CustomOAuth2Exception extends OAuth2AuthenticationException {
    public CustomOAuth2Exception(String errorCode) {
        super(errorCode);
    }
}
