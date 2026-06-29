package com.tickets.msauth.service.impl;

import com.tickets.msauth.dto.request.LoginRequest;
import com.tickets.msauth.dto.request.RegisterRequest;
import com.tickets.msauth.dto.response.AuthResponse;
import com.tickets.msauth.entity.User;
import com.tickets.msauth.exception.InvalidCredentialsException;
import com.tickets.msauth.exception.UserAlreadyExistsException;
import com.tickets.msauth.repository.UserRepository;
import com.tickets.msauth.security.JwtUtil;
import com.tickets.msauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link AuthService}.
 * Handles user registration, login, and JWT token validation.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * {@inheritDoc}
     * Encodes the password with BCrypt before persisting the user.
     */
    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("El usuario '" + request.username() + "' ya existe");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("El email '" + request.email() + "' ya esta registrado");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role("USER")
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole(),
                "Usuario registrado exitosamente");
    }

    /**
     * {@inheritDoc}
     * Verifies the password using BCrypt before issuing a token.
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales invalidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales invalidas");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole(), "Login exitoso");
    }

    /** {@inheritDoc} */
    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
