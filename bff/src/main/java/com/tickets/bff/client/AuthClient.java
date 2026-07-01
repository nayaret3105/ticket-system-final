package com.tickets.bff.client;

import com.tickets.bff.dto.AuthResponseDto;
import com.tickets.bff.dto.LoginRequestDto;
import com.tickets.bff.dto.MessageResponseDto;
import com.tickets.bff.dto.RegisterRequestDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

    private final RestClient restClient;

    public AuthClient(@Qualifier("authRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<MessageResponseDto> register(RegisterRequestDto dto) {
        return restClient.post()
                .uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toEntity(MessageResponseDto.class);
    }

    public ResponseEntity<AuthResponseDto> login(LoginRequestDto dto) {
        return restClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toEntity(AuthResponseDto.class);
    }
}
