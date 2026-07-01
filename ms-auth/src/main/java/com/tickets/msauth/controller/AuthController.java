package com.tickets.msauth.controller;

import com.tickets.msauth.dto.AuthResponseDto;
import com.tickets.msauth.dto.LoginRequestDto;
import com.tickets.msauth.dto.MessageResponseDto;
import com.tickets.msauth.dto.RegisterRequestDto;
import com.tickets.msauth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and token validation")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register user",
            description = "Creates a new user account in the authentication service.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration data",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration request"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> register(@Valid @RequestBody RegisterRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates user credentials and returns a JWT token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login credentials",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid login request"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(
            summary = "Validate JWT token",
            description = "Validates a JWT token and returns whether it is valid.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JWT token to validate",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token validation completed"),
            @ApiResponse(responseCode = "400", description = "Invalid validation request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        boolean valid = authService.validateToken(token);
        return valid ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
}
