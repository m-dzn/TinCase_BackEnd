package com.appleisle.tincase.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2InvalidProviderException extends AuthenticationException {

    public OAuth2InvalidProviderException(String registrationId) {
        super(registrationId);
    }

}
