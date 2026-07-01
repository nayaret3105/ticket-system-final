package com.tickets.bff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
    @NotBlank(message = "El nombre de usuario es obligatorio") String username,
    @Email(message = "Email invalido") @NotBlank(message = "El email es obligatorio") String email,
    @NotBlank(message = "La contraseña es obligatoria") String password
) {}
