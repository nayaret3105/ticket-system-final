package com.tickets.bff.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequestDto(
    @NotBlank(message = "El nombre del evento es obligatorio") String name,
    String description,
    @NotNull(message = "La fecha es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Schema(type = "string", example = "2026-06-30 21:59", pattern = "yyyy-MM-dd HH:mm") LocalDateTime eventDate,
    @NotBlank(message = "La ubicacion es obligatoria") String location,
    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad minima es 1") Integer capacity,
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0") BigDecimal ticketPrice
) {}
