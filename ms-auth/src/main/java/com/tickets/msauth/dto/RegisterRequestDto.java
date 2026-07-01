package com.tickets.msauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
    @NotBlank(message = "El nombre de usuario es obligatorio") String username,
    @Email(message = "El email debe ser valido")
    @NotBlank(message = "El email es obligatorio") String email,
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password
) {}
