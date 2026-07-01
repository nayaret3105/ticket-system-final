package com.tickets.msevent.controller;

import com.tickets.msevent.dto.EventRequestDto;
import com.tickets.msevent.dto.EventResponseDto;
import com.tickets.msevent.dto.MessageResponseDto;
import com.tickets.msevent.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Endpoints for event management")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "List all events", description = "Returns all events ordered by date ascending.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> listar() {
        return ResponseEntity.ok(eventService.listarTodos());
    }

    @Operation(summary = "Find event by ID", description = "Returns event information using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> buscarPorId(
            @Parameter(description = "Event unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(eventService.buscarPorId(id));
    }

    @Operation(
            summary = "Create event",
            description = "Registers a new event. Validates duplicates by name and date.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Event data to create",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid event request"),
            @ApiResponse(responseCode = "409", description = "Event already exists with same name and date"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<EventResponseDto> crear(@Valid @RequestBody EventRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.crear(dto));
    }

    @Operation(
            summary = "Update event",
            description = "Updates an existing event by its unique identifier.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated event data",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid event request"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> actualizar(
            @Parameter(description = "Event unique identifier", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody EventRequestDto dto) {
        return ResponseEntity.ok(eventService.actualizar(id, dto));
    }

    @Operation(summary = "Delete event", description = "Permanently removes an event from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> eliminar(
            @Parameter(description = "Event unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(eventService.eliminar(id));
    }
}
