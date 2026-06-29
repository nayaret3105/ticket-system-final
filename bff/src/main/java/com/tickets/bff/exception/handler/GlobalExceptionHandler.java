package com.tickets.bff.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleClientError(HttpClientErrorException ex) {
        log.error("Error de cliente desde microservicio: {} - {}", ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of(
                        "status", ex.getStatusCode().value(),
                        "message", ex.getResponseBodyAsString()
                ));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleServerError(HttpServerErrorException ex) {
        log.error("Error de servidor desde microservicio: {} - {}", ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of(
                        "status", ex.getStatusCode().value(),
                        "message", "Error en el microservicio: " + ex.getResponseBodyAsString()
                ));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleConnectionError(ResourceAccessException ex) {
        log.error("Error de conexion con microservicio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "status", 503,
                        "message", "Microservicio no disponible temporalmente"
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(Map.of("status", 400, "message", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        log.error("Error inesperado en BFF: {}", ex.getMessage(), ex);
        return ResponseEntity.internalServerError()
                .body(Map.of("status", 500, "message", "Error interno del servidor"));
    }
}
