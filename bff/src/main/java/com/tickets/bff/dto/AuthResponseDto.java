package com.tickets.bff.dto;

public record AuthResponseDto(
    String token,
    String username,
    String email,
    String role,
    String message
) {}
