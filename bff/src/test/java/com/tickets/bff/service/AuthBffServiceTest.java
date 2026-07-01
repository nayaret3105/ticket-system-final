package com.tickets.bff.service;

import com.tickets.bff.client.AuthClient;
import com.tickets.bff.dto.AuthResponseDto;
import com.tickets.bff.dto.LoginRequestDto;
import com.tickets.bff.dto.MessageResponseDto;
import com.tickets.bff.dto.RegisterRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthBffServiceTest {

    @Mock
    private AuthClient authClient;

    @Test
    void registerShouldDelegateToClient() {
        AuthBffService service = new AuthBffService(authClient);
        RegisterRequestDto request = new RegisterRequestDto("testuser", "test@example.com", "password123");
        ResponseEntity<MessageResponseDto> response = ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponseDto("Usuario registrado correctamente: testuser"));
        when(authClient.register(request)).thenReturn(response);

        ResponseEntity<MessageResponseDto> result = service.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Usuario registrado correctamente: testuser", result.getBody().message());
    }

    @Test
    void loginShouldDelegateToClient() {
        AuthBffService service = new AuthBffService(authClient);
        LoginRequestDto request = new LoginRequestDto("testuser", "password123");
        ResponseEntity<AuthResponseDto> response = ResponseEntity.ok(
                new AuthResponseDto("jwt-token", "testuser", "test@example.com", "USER", "Login exitoso"));
        when(authClient.login(request)).thenReturn(response);

        ResponseEntity<AuthResponseDto> result = service.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("jwt-token", result.getBody().token());
    }
}
