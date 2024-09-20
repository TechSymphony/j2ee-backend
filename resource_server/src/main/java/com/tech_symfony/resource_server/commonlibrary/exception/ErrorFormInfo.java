package com.tech_symfony.resource_server.commonlibrary.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class ErrorFormInfo {
	private final String className;
	private final String timestamp;
	private List<Object> errors;

	public ErrorFormInfo(Exception ex) {
		this.className = ex.getClass().getName();
		this.errors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<>();
		error.put("exMessage", ex.getLocalizedMessage());
		this.errors.add(error);
		this.timestamp = String.valueOf(System.currentTimeMillis());
	}

	public ErrorFormInfo(Exception ex, List<Object> errors) {
		this.className = ex.getClass().getName();
		this.errors = errors;
		this.timestamp = String.valueOf(System.currentTimeMillis());
	}
}
