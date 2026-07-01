package com.tickets.mssales.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketSaleResponseDto(
    UUID id,
    UUID eventId,
    String buyer,
    Integer ticketCount,
    BigDecimal total,
    LocalDateTime saleDate
) {}
