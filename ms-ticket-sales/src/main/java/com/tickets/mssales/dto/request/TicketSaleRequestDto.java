package com.tickets.mssales.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO carrying the data required to register a ticket sale.
 */
public record TicketSaleRequestDto(
        @NotNull(message = "El ID del evento es obligatorio")
        UUID eventId,

        @NotBlank(message = "El nombre del comprador es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String buyer,

        @NotNull(message = "La cantidad de entradas es obligatoria")
        @Min(value = 1, message = "Debe comprar al menos 1 entrada")
        Integer ticketCount
) {}
