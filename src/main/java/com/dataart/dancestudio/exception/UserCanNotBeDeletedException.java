package com.dataart.dancestudio.exception;

public class UserCanNotBeDeletedException extends RuntimeException {

    public UserCanNotBeDeletedException(final String message) {
        super(message);
    }

}
