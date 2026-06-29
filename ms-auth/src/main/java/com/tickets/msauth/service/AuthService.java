package com.tickets.msauth.service;

import com.tickets.msauth.dto.request.LoginRequest;
import com.tickets.msauth.dto.request.RegisterRequest;
import com.tickets.msauth.dto.response.AuthResponse;

/**
 * Contract for authentication operations: register, login, and token validation.
 */
public interface AuthService {

    /**
     * Registers a new user and returns a JWT token.
     *
     * @param request the registration data (username, email, password)
     * @return an {@link AuthResponse} containing the generated token and user details
     * @throws com.tickets.msauth.exception.UserAlreadyExistsException if the username or email is already taken
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request the login credentials
     * @return an {@link AuthResponse} containing the generated token and user details
     * @throws com.tickets.msauth.exception.InvalidCredentialsException if the credentials are invalid
     */
    AuthResponse login(LoginRequest request);

    /**
     * Validates whether a JWT token is well-formed and has not expired.
     *
     * @param token the raw JWT string (without "Bearer " prefix)
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    boolean validateToken(String token);
}
