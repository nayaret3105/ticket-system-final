package com.tickets.bff.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO carrying login credentials forwarded to ms-auth.
 */
public record AuthLoginRequestDto(
        @NotBlank(message = "El username es obligatorio") String username,
        @NotBlank(message = "La contrasena es obligatoria") String password
) {}
