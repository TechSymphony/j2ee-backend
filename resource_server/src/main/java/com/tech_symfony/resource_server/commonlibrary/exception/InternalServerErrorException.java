package com.tech_symfony.resource_server.commonlibrary.exception;

import com.tech_symfony.resource_server.commonlibrary.utils.MessagesUtils;

public class InternalServerErrorException extends RuntimeException {

    private final String message;

    public InternalServerErrorException(String errorCode, Object... var2) {
        this.message = MessagesUtils.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
