package com.tickets.bff.dto;

/**
 * DTO received from ms-auth after a successful login or registration.
 */
public record AuthResponseDto(
        String token,
        String username,
        String email,
        String role,
        String message
) {}
