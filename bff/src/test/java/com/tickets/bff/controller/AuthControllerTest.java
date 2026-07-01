package com.tickets.bff.controller;

import com.tickets.bff.dto.AuthResponseDto;
import com.tickets.bff.dto.LoginRequestDto;
import com.tickets.bff.dto.MessageResponseDto;
import com.tickets.bff.dto.RegisterRequestDto;
import com.tickets.bff.service.AuthBffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthBffService authBffService;

    @Test
    void registerShouldReturnCreated() {
        AuthController controller = new AuthController(authBffService);
        RegisterRequestDto request = new RegisterRequestDto("testuser", "test@example.com", "password123");
        ResponseEntity<MessageResponseDto> response = ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponseDto("Usuario registrado correctamente: testuser"));
        when(authBffService.register(any())).thenReturn(response);

        ResponseEntity<MessageResponseDto> result = controller.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void loginShouldReturnOk() {
        AuthController controller = new AuthController(authBffService);
        LoginRequestDto request = new LoginRequestDto("testuser", "password123");
        ResponseEntity<AuthResponseDto> response = ResponseEntity.ok(
                new AuthResponseDto("jwt-token", "testuser", "test@example.com", "USER", "Login exitoso"));
        when(authBffService.login(any())).thenReturn(response);

        ResponseEntity<AuthResponseDto> result = controller.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
}
