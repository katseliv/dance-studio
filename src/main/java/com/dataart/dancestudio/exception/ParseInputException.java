package com.dataart.dancestudio.exception;

public class ParseInputException extends RuntimeException {

    public ParseInputException(final String message) {
        super(message);
    }

    public ParseInputException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
