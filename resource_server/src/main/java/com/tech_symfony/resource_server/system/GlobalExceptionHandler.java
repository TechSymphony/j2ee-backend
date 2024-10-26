package com.tech_symfony.resource_server.system;

import com.tech_symfony.resource_server.commonlibrary.exception.BadRequestException;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import com.tech_symfony.resource_server.commonlibrary.viewmodel.error.ErrorVm;
import com.tech_symfony.resource_server.system.payment.vnpay.TransactionException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorVm> handleTransactionException(TransactionException ex, WebRequest request) {
        HttpStatus status = BAD_REQUEST; // You can customize the status based on your logic
        String errorMessage = ex.getError();
        String statusCode = String.valueOf(ex.getStatus());
        List<String> messages = ex.getMessages();

        // Create the ErrorVm object
        ErrorVm errorResponse = new ErrorVm(errorMessage, statusCode, messages.toString());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorVm> handleNotFoundException(NotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request, 404);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorVm> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return handleBadRequest(ex, request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorVm> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildErrorResponse(status, "Request information is not valid", errors, ex, null, 0);
    }

//    /**
//     * Handles exception thrown by Bean Validation on controller methods parameters
//     *
//     * @param e       The thrown exception
//     * @param request the current web request
//     * @return an empty response entity
//     */
//    @ExceptionHandler({ConstraintViolationException.class})
//    @ResponseStatus(code = BAD_REQUEST)
//    @ResponseBody
//    public ErrorFormInfo handlerException(ConstraintViolationException e, WebRequest request) {
//        final List<Object> errors = new ArrayList<>();
//        e.getConstraintViolations().stream().forEach(fieldError -> {
//            Map<String, Object> error = new HashMap<>();
//            error.put("path", String.valueOf(fieldError.getPropertyPath()));
//            error.put("message", fieldError.getMessage());
//            errors.add(error);
//        });
//
//        return new ErrorFormInfo(e, errors);
//    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorVm> handleConstraintViolation(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> String.format("%s: %s",
                        violation.getPropertyPath(),
                        violation.getMessage()))
                .toList();

        return buildErrorResponse(status, "Request information is not valid", errors, ex, null, 0);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorVm> handleOtherException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request, 500);
    }

    private String getServletPath(WebRequest webRequest) {
        ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
        return servletRequest.getRequest().getServletPath();
    }

    private ResponseEntity<ErrorVm> handleBadRequest(Exception ex, WebRequest request) {
        HttpStatus status = BAD_REQUEST;
        String message = ex.getMessage();

        return buildErrorResponse(status, message, null, ex, request, 400);
    }

    private ResponseEntity<ErrorVm> buildErrorResponse(HttpStatus status, String message, List<String> errors,
                                                       Exception ex, WebRequest request, int statusCode) {
        ErrorVm errorVm =
                new ErrorVm(status.toString(), status.getReasonPhrase(), message, errors);

        if (request != null) {
            log.error(ERROR_LOG_FORMAT, this.getServletPath(request), statusCode, message);
        }
        log.error(message, ex);
        return ResponseEntity.status(status).body(errorVm);
    }

}
