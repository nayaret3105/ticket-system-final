package com.tickets.msauth.controller;

import com.tickets.msauth.dto.AuthResponseDto;
import com.tickets.msauth.dto.LoginRequestDto;
import com.tickets.msauth.dto.MessageResponseDto;
import com.tickets.msauth.dto.RegisterRequestDto;
import com.tickets.msauth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Test
    void registerShouldReturnCreated() {
        AuthController controller = new AuthController(authService);
        RegisterRequestDto request = new RegisterRequestDto("testuser", "test@example.com", "password123");
        MessageResponseDto response = new MessageResponseDto("Usuario registrado correctamente: testuser");
        when(authService.register(any())).thenReturn(response);

        ResponseEntity<MessageResponseDto> result = controller.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Usuario registrado correctamente: testuser", result.getBody().message());
    }

    @Test
    void loginShouldReturnOk() {
        AuthController controller = new AuthController(authService);
        LoginRequestDto request = new LoginRequestDto("testuser", "password123");
        AuthResponseDto response = new AuthResponseDto("jwt-token", "testuser", "test@example.com", "USER", "Login exitoso");
        when(authService.login(any())).thenReturn(response);

        ResponseEntity<AuthResponseDto> result = controller.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("jwt-token", result.getBody().token());
    }

    @Test
    void validateShouldReturnOkWhenTokenIsValid() {
        AuthController controller = new AuthController(authService);
        when(authService.validateToken("valid-token")).thenReturn(true);

        ResponseEntity<Boolean> result = controller.validate("Bearer valid-token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody());
    }

    @Test
    void validateShouldReturnUnauthorizedWhenTokenIsInvalid() {
        AuthController controller = new AuthController(authService);
        when(authService.validateToken("invalid-token")).thenReturn(false);

        ResponseEntity<Boolean> result = controller.validate("Bearer invalid-token");

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }
}
