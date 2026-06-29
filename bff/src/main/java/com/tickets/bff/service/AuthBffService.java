package com.tickets.bff.service;

import com.tickets.bff.client.AuthClient;
import com.tickets.bff.dto.AuthLoginRequestDto;
import com.tickets.bff.dto.AuthRegisterRequestDto;
import com.tickets.bff.dto.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * BFF service that delegates authentication requests to ms-auth.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthBffService {

    private final AuthClient authClient;

    public ResponseEntity<AuthResponseDto> register(AuthRegisterRequestDto request) {
        log.debug("BFF: delegando registro de usuario '{}'", request.username());
        return authClient.register(request);
    }

    public ResponseEntity<AuthResponseDto> login(AuthLoginRequestDto request) {
        log.debug("BFF: delegando login de usuario '{}'", request.username());
        return authClient.login(request);
    }
}
