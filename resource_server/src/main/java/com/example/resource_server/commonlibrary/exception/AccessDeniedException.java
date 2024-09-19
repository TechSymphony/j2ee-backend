package com.example.resource_server.commonlibrary.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(final String message) {
        super(message);
    }
}
