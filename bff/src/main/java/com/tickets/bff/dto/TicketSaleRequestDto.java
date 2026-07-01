package com.tickets.bff.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record TicketSaleRequestDto(
    @NotNull(message = "El ID del evento es obligatorio") UUID eventId,
    @NotBlank(message = "El nombre del comprador es obligatorio") String buyer,
    @NotNull(message = "La cantidad de entradas es obligatoria")
    @Min(value = 1, message = "Debe comprar al menos 1 entrada") Integer ticketCount
) {}
