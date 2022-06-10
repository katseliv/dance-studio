package com.dataart.dancestudio.exception;

public class DecodeJwtTokenException extends RuntimeException {

    public DecodeJwtTokenException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
