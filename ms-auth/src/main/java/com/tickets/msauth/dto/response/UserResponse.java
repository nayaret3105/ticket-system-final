package com.tickets.msauth.dto.response;

import java.time.LocalDateTime;

/**
 * DTO representing a user's public profile data.
 */
public record UserResponse(Long id, String username, String email, String role, LocalDateTime createdAt) {}
