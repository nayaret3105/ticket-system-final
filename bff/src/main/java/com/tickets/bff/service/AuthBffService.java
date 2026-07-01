package com.tickets.bff.service;

import com.tickets.bff.client.AuthClient;
import com.tickets.bff.dto.AuthResponseDto;
import com.tickets.bff.dto.LoginRequestDto;
import com.tickets.bff.dto.MessageResponseDto;
import com.tickets.bff.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthBffService {

    private final AuthClient authClient;

    public ResponseEntity<MessageResponseDto> register(RegisterRequestDto dto) {
        return authClient.register(dto);
    }

    public ResponseEntity<AuthResponseDto> login(LoginRequestDto dto) {
        return authClient.login(dto);
    }
}
