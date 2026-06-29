package com.tickets.bff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO carrying registration data forwarded to ms-auth.
 */
public record AuthRegisterRequestDto(
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
        String username,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene formato valido")
        String email,

        @NotBlank(message = "La contrasena es obligatoria")
        @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
        String password
) {}
