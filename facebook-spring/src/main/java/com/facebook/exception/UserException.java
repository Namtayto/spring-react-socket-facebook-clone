package com.facebook.exception;

import lombok.Getter;

public class UserException extends Exception {

    public UserException(String message) {
        super(message);
    }
}
