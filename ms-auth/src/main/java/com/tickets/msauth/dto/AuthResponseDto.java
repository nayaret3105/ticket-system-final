package com.tickets.msauth.dto;

public record AuthResponseDto(
    String token,
    String username,
    String email,
    String role,
    String message
) {}
