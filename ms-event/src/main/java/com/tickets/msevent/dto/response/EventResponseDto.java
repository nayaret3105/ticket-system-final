package com.tickets.msevent.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO returned by the API with the full details of an event.
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
