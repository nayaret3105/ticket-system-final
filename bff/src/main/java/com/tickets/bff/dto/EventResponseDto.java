package com.tickets.bff.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO received from ms-event representing a full event record.
 */
public record EventResponseDto(
        UUID id,
        String name,
        String description,
        LocalDateTime eventDate,
        String location,
        Integer capacity,
        BigDecimal ticketPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
