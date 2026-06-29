package com.tickets.msauth.dto.response;

/**
 * DTO returned after a successful registration or login, containing the JWT token.
 */
public record AuthResponse(
        String token,
        String username,
        String email,
        String role,
        String message
) {}
