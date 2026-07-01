package com.tickets.msevent.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationForValidData() {
        EventRequestDto dto = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now().plusDays(1),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"));

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidationWhenCapacityIsZero() {
        EventRequestDto dto = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now().plusDays(1),
                "Estadio Nacional", 0, new BigDecimal("25000.00"));

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertEquals(1, violations.size());
        assertTrue(fields.contains("capacity"));
    }

    @Test
    void shouldFailValidationWhenTicketPriceIsZeroOrLess() {
        EventRequestDto dto = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now().plusDays(1),
                "Estadio Nacional", 5000, BigDecimal.ZERO);

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertEquals(1, violations.size());
        assertTrue(fields.contains("ticketPrice"));
    }

    @Test
    void shouldFailValidationForMissingRequiredFields() {
        EventRequestDto dto = new EventRequestDto("", null, null, "", null, null);

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertFalse(violations.isEmpty());
        assertTrue(fields.contains("name"));
        assertTrue(fields.contains("eventDate"));
        assertTrue(fields.contains("location"));
        assertTrue(fields.contains("capacity"));
        assertTrue(fields.contains("ticketPrice"));
    }
}
