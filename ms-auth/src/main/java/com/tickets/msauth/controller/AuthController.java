package com.tickets.msauth.controller;

import com.tickets.msauth.dto.request.LoginRequest;
import com.tickets.msauth.dto.request.RegisterRequest;
import com.tickets.msauth.dto.response.AuthResponse;
import com.tickets.msauth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Endpoints para registro, login y validacion de tokens")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario en el sistema y retorna un token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de registro invalidos"),
        @ApiResponse(responseCode = "409", description = "El usuario o email ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Autentica al usuario y retorna un token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "400", description = "Datos de login invalidos"),
        @ApiResponse(responseCode = "401", description = "Credenciales invalidas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar token JWT", description = "Verifica si un token JWT es valido y no ha expirado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token valido"),
        @ApiResponse(responseCode = "401", description = "Token invalido o expirado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        boolean valid = authService.validateToken(token);
        return valid ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
}
