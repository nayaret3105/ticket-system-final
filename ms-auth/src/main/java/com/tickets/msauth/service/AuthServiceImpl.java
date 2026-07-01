package com.tickets.msauth.service;

import com.tickets.msauth.dto.AuthResponseDto;
import com.tickets.msauth.dto.LoginRequestDto;
import com.tickets.msauth.dto.MessageResponseDto;
import com.tickets.msauth.dto.RegisterRequestDto;
import com.tickets.msauth.model.User;
import com.tickets.msauth.exception.InvalidCredentialsException;
import com.tickets.msauth.exception.UserAlreadyExistsException;
import com.tickets.msauth.repository.UserRepository;
import com.tickets.msauth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public MessageResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new UserAlreadyExistsException("El usuario ya existe: " + dto.username());
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("El email ya esta registrado: " + dto.email());
        }
        userRepository.save(User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role("USER")
                .build());
        return new MessageResponseDto("Usuario registrado correctamente: " + dto.username());
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales invalidas"));
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales invalidas");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponseDto(token, user.getUsername(), user.getEmail(), user.getRole(), "Login exitoso");
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
