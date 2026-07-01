package com.tickets.mssales.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketSaleRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationForValidData() {
        TicketSaleRequestDto dto = new TicketSaleRequestDto(UUID.randomUUID(), "Juan Perez", 2);

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidationWhenTicketCountIsZero() {
        TicketSaleRequestDto dto = new TicketSaleRequestDto(UUID.randomUUID(), "Juan Perez", 0);

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertEquals(1, violations.size());
        assertTrue(fields.contains("ticketCount"));
    }

    @Test
    void shouldFailValidationForMissingRequiredFields() {
        TicketSaleRequestDto dto = new TicketSaleRequestDto(null, "", null);

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertFalse(violations.isEmpty());
        assertEquals(3, fields.size());
        assertTrue(fields.contains("eventId"));
        assertTrue(fields.contains("buyer"));
        assertTrue(fields.contains("ticketCount"));
    }
}
