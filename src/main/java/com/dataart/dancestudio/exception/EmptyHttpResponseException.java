package com.dataart.dancestudio.exception;

public class EmptyHttpResponseException extends RuntimeException {

    public EmptyHttpResponseException(final String message) {
        super(message);
    }

}
