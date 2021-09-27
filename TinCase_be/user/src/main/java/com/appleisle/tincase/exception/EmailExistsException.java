package com.appleisle.tincase.exception;

import lombok.Getter;

@Getter
public class EmailExistsException extends RuntimeException {

    private String email;

    public EmailExistsException(String email) {
        this.email = email;
    }

}
