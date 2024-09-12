package com.example.resource_server.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbidenMethodControllerException extends RuntimeException {
	public ForbidenMethodControllerException(String message) {
		super(message);
	}
}
