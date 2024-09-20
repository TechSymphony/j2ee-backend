package com.tech_symfony.resource_server.commonlibrary.exception;

public class CreateGuestUserException extends RuntimeException {
    public CreateGuestUserException(final String message) {
        super(message);
    }
}
