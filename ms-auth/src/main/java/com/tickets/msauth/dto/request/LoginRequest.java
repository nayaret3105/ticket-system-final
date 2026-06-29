package com.tickets.msauth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO carrying the credentials submitted on login.
 */
public record LoginRequest(
        @NotBlank(message = "El username es obligatorio") String username,
        @NotBlank(message = "La contrasena es obligatoria") String password
) {}
