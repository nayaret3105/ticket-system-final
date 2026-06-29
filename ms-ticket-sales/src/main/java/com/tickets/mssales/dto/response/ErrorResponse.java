package com.tickets.mssales.dto.response;

/**
 * Standard error response returned by the global exception handler.
 */
public record ErrorResponse(int status, String message) {}
