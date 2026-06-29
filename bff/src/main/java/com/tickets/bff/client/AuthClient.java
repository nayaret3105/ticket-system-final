package com.tickets.bff.client;

import com.tickets.bff.dto.AuthLoginRequestDto;
import com.tickets.bff.dto.AuthRegisterRequestDto;
import com.tickets.bff.dto.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class AuthClient {

    private final RestClient restClient;

    public AuthClient(@Qualifier("authRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<AuthResponseDto> register(AuthRegisterRequestDto request) {
        log.debug("Llamando a ms-auth: POST /auth/register");
        return restClient.post()
                .uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(AuthResponseDto.class);
    }

    public ResponseEntity<AuthResponseDto> login(AuthLoginRequestDto request) {
        log.debug("Llamando a ms-auth: POST /auth/login");
        return restClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(AuthResponseDto.class);
    }
}
