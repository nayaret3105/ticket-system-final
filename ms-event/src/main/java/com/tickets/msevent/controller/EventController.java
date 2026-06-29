package com.tickets.msevent.controller;

import com.tickets.msevent.dto.request.EventRequestDto;
import com.tickets.msevent.dto.response.EventResponseDto;
import com.tickets.msevent.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "CRUD completo de eventos")
@SecurityRequirement(name = "Bearer")
public class EventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "Listar todos los eventos", description = "Retorna todos los eventos ordenados por fecha ascendente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de eventos obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<EventResponseDto>> getAll() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evento por ID", description = "Retorna los datos completos de un evento especifico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EventResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    @Operation(summary = "Crear evento", description = "Registra un nuevo evento. Valida duplicados por nombre y fecha.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del evento invalidos"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "409", description = "Ya existe un evento con ese nombre y fecha"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EventResponseDto> create(@Valid @RequestBody EventRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento", description = "Actualiza todos los campos de un evento existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del evento invalidos"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EventResponseDto> update(@PathVariable UUID id,
                                                   @Valid @RequestBody EventRequestDto request) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evento", description = "Elimina permanentemente un evento del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
