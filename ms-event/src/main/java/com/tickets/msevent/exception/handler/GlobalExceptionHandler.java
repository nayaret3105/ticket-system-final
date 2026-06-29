package com.tickets.msevent.exception.handler;

import com.tickets.msevent.dto.response.ErrorResponse;
import com.tickets.msevent.exception.EventAlreadyExistsException;
import com.tickets.msevent.exception.EventNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Centralised error handler for ms-event. Converts domain exceptions
 * into a consistent {@link ErrorResponse} body.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEventNotFound(EventNotFoundException ex) {
        return new ErrorResponse(404, ex.getMessage());
    }

    @ExceptionHandler(EventAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEventAlreadyExists(EventAlreadyExistsException ex) {
        return new ErrorResponse(409, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ErrorResponse(400, message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        return new ErrorResponse(500, "Error interno del servidor");
    }
}
