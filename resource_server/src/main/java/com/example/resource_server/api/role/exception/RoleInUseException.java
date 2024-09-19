package com.example.resource_server.api.role.exception;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class RoleInUseException extends RuntimeException {
	private final Timestamp timestamp;
	private final String httpStatus;
	private final int statusCode;
	private final String error;
	private final List<String> messages;

	public RoleInUseException(Integer roleId) {
		super("Role with id " + roleId + " is in use and cannot be deleted.");
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.httpStatus = "CONFLICT";
		this.statusCode = 409;
		this.error = "Role In Use";
		this.messages = List.of("Role with id " + roleId + " is in use and cannot be deleted.");
	}

}
