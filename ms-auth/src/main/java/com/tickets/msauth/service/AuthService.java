package com.tickets.msauth.service;

import com.tickets.msauth.dto.AuthResponseDto;
import com.tickets.msauth.dto.LoginRequestDto;
import com.tickets.msauth.dto.MessageResponseDto;
import com.tickets.msauth.dto.RegisterRequestDto;

public interface AuthService {
    MessageResponseDto register(RegisterRequestDto dto);
    AuthResponseDto login(LoginRequestDto dto);
    boolean validateToken(String token);
}
