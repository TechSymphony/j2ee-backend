package com.example.resource_server.api.role.exception;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class RoleNotFoundException extends RuntimeException {
	private final Timestamp timestamp;
	private final String httpStatus;
	private final int statusCode;
	private final String error;
	private final List<String> messages;

	public RoleNotFoundException(Integer id) {
		super("Could not find role " + id);
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.httpStatus = "NOT_FOUND";
		this.statusCode = 404;
		this.error = "Role Not Found";
		this.messages = List.of("Could not find role " + id);
	}

}
