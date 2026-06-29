package com.tickets.bff.controller;

import com.tickets.bff.dto.AuthLoginRequestDto;
import com.tickets.bff.dto.AuthRegisterRequestDto;
import com.tickets.bff.dto.AuthResponseDto;
import com.tickets.bff.service.AuthBffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth BFF", description = "Proxy de autenticacion hacia ms-auth")
public class AuthBffController {

    private final AuthBffService authBffService;

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario y retorna un token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de registro invalidos"),
        @ApiResponse(responseCode = "409", description = "El usuario o email ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody AuthRegisterRequestDto request) {
        return authBffService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Autentica al usuario y retorna un token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "400", description = "Datos de login invalidos"),
        @ApiResponse(responseCode = "401", description = "Credenciales invalidas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthLoginRequestDto request) {
        return authBffService.login(request);
    }
}
