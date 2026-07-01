package com.tickets.bff.controller;

import com.tickets.bff.dto.AuthResponseDto;
import com.tickets.bff.dto.LoginRequestDto;
import com.tickets.bff.dto.MessageResponseDto;
import com.tickets.bff.dto.RegisterRequestDto;
import com.tickets.bff.service.AuthBffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthBffService authBffService;

    @Operation(
            summary = "Register user",
            description = "Creates a new user account.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration data", required = true
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
        return authBffService.register(dto);
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates user credentials and returns a JWT token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login credentials", required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        return authBffService.login(dto);
    }
}
