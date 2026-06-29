package com.tickets.bff.controller;

import com.tickets.bff.dto.EventRequestDto;
import com.tickets.bff.dto.EventResponseDto;
import com.tickets.bff.service.EventBffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Eventos BFF", description = "Proxy de gestion de eventos hacia ms-event")
@SecurityRequirement(name = "Bearer")
public class EventBffController {

    private final EventBffService eventBffService;

    @GetMapping
    @Operation(summary = "Listar todos los eventos", description = "Retorna la lista completa de eventos ordenados por fecha")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de eventos obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<EventResponseDto>> getAll(@RequestHeader("Authorization") String authHeader) {
        return eventBffService.getAllEvents(authHeader);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evento por ID", description = "Retorna los datos de un evento especifico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EventResponseDto> getById(@PathVariable UUID id,
                                                    @RequestHeader("Authorization") String authHeader) {
        return eventBffService.getEventById(id, authHeader);
    }

    @PostMapping
    @Operation(summary = "Crear evento", description = "Registra un nuevo evento en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del evento invalidos"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "409", description = "Ya existe un evento con ese nombre y fecha"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EventResponseDto> create(@Valid @RequestBody EventRequestDto request,
                                                   @RequestHeader("Authorization") String authHeader) {
        return eventBffService.createEvent(request, authHeader);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento", description = "Actualiza los datos de un evento existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del evento invalidos"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EventResponseDto> update(@PathVariable UUID id,
                                                   @Valid @RequestBody EventRequestDto request,
                                                   @RequestHeader("Authorization") String authHeader) {
        return eventBffService.updateEvent(id, request, authHeader);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evento", description = "Elimina un evento del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @RequestHeader("Authorization") String authHeader) {
        return eventBffService.deleteEvent(id, authHeader);
    }
}
