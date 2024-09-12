package com.example.resource_server.system.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler {


	/**
	 * Handles exception thrown by Bean Validation on controller methods parameters
	 *
	 * @param e       The thrown exception
	 * @param request the current web request
	 * @return an empty response entity
	 */
	@ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
	@ResponseStatus(code = BAD_REQUEST)
	@ResponseBody
	public ErrorInfo handlerException(ConstraintViolationException e, WebRequest request) {
		final List<Object> errors = new ArrayList<>();
		e.getConstraintViolations().stream().forEach(fieldError -> {
			Map<String, Object> error = new HashMap<>();
			error.put("path", String.valueOf(fieldError.getPropertyPath()));
			error.put("message", fieldError.getMessage());
			errors.add(error);
		});

		return new ErrorInfo(e, errors);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorInfo accessDenied(Exception e) {
		return new ErrorInfo(e);
	}


	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorInfo exception(Exception e) {
		return new ErrorInfo(e);
	}


	//	public ResponseEntity<String> exception(Exception e) {
//		ObjectMapper mapper = new ObjectMapper();
//		ErrorInfo errorInfo = new ErrorInfo(e);
//		String respJSONstring = "{}";
//		try {
//			respJSONstring = mapper.writeValueAsString(errorInfo);
//		} catch (JsonProcessingException e1) {
//			e1.printStackTrace();
//		}
//
//		ResponseEntity.BodyBuilder responseWithStatus;
//		if (e instanceof AccessDeniedException) {
//			responseWithStatus = ResponseEntity.status(HttpStatus.NOT_FOUND);
//		} else {
//			responseWithStatus = ResponseEntity.status(HttpStatus.BAD_REQUEST);
//		}
//
//		return responseWithStatus.body(respJSONstring);
//	}

}
