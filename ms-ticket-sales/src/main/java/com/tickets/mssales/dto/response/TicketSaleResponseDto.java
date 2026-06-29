package com.tickets.mssales.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO returned by the API with the full details of a ticket sale.
 */
public record TicketSaleResponseDto(
        UUID id,
        UUID eventId,
        String buyer,
        Integer ticketCount,
        BigDecimal total,
        LocalDateTime saleDate
) {}
