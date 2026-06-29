package com.tickets.msevent.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO carrying the data required to create or update an event.
 */
public record EventRequestDto(
        @NotBlank(message = "El nombre del evento es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String name,

        String description,

        @NotNull(message = "La fecha es obligatoria")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime eventDate,

        @NotBlank(message = "La ubicacion es obligatoria")
        String location,

        @NotNull(message = "La capacidad es obligatoria")
        @Min(value = 1, message = "La capacidad minima es 1")
        Integer capacity,

        @NotNull(message = "El precio de entrada es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal ticketPrice
) {}
