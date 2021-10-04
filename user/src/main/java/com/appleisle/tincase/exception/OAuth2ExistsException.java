package com.appleisle.tincase.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2ExistsException extends AuthenticationException {

    public OAuth2ExistsException(String registrationId) {
        super(registrationId);
    }

}