package com.tickets.bff.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationForValidData() {
        RegisterRequestDto dto = new RegisterRequestDto("testuser", "test@example.com", "password123");

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        RegisterRequestDto dto = new RegisterRequestDto("testuser", "not-an-email", "password123");

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertEquals(1, violations.size());
        assertTrue(fields.contains("email"));
    }

    @Test
    void shouldFailValidationForBlankFields() {
        RegisterRequestDto dto = new RegisterRequestDto("", "", "");

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertFalse(violations.isEmpty());
        assertEquals(3, fields.size());
        assertTrue(fields.contains("username"));
        assertTrue(fields.contains("email"));
        assertTrue(fields.contains("password"));
    }
}
