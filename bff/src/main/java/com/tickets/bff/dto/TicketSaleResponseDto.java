package com.tickets.bff.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO received from ms-ticket-sales representing a full sale record.
 */
public record TicketSaleResponseDto(
        UUID id,
        UUID eventId,
        String buyer,
        Integer ticketCount,
        BigDecimal total,
        LocalDateTime saleDate
) {}
