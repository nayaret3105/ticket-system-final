package com.tickets.mssales.exception.handler;

import com.tickets.mssales.dto.response.ErrorResponse;
import com.tickets.mssales.exception.BusinessException;
import com.tickets.mssales.exception.SaleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Centralised error handler for ms-ticket-sales. Converts domain exceptions
 * into a consistent {@link ErrorResponse} body.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleSaleNotFound(SaleNotFoundException ex) {
        return new ErrorResponse(404, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusinessException(BusinessException ex) {
        return new ErrorResponse(400, ex.getMessage());
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
