package com.appleisle.tincase.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2ProcessingException extends AuthenticationException {

    public OAuth2ProcessingException(String registrationId) {
        super(registrationId);
    }

}
