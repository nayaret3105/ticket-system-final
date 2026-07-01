package com.tickets.bff.controller;

import com.tickets.bff.dto.EventRequestDto;
import com.tickets.bff.dto.EventResponseDto;
import com.tickets.bff.dto.MessageResponseDto;
import com.tickets.bff.service.EventBffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Proxy for event management towards ms-event")
public class EventBffController {

    private final EventBffService eventBffService;

    @Operation(summary = "List all events", description = "Returns the complete list of events ordered by date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> listar(@RequestHeader("Authorization") String authHeader) {
        return eventBffService.getAllEvents(authHeader);
    }

    @Operation(summary = "Find event by ID", description = "Returns data of a specific event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> buscarPorId(
            @Parameter(description = "Event unique identifier", required = true) @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        return eventBffService.getEventById(id, authHeader);
    }

    @Operation(
            summary = "Create event",
            description = "Registers a new event in the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Event data to create", required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid event data"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "409", description = "Event already exists with same name and date"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<EventResponseDto> crear(
            @Valid @RequestBody EventRequestDto dto,
            @RequestHeader("Authorization") String authHeader) {
        return eventBffService.createEvent(dto, authHeader);
    }

    @Operation(
            summary = "Update event",
            description = "Updates an existing event.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated event data", required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid event data"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> actualizar(
            @Parameter(description = "Event unique identifier", required = true) @PathVariable UUID id,
            @Valid @RequestBody EventRequestDto dto,
            @RequestHeader("Authorization") String authHeader) {
        return eventBffService.updateEvent(id, dto, authHeader);
    }

    @Operation(summary = "Delete event", description = "Removes an event from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> eliminar(
            @Parameter(description = "Event unique identifier", required = true) @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        return eventBffService.deleteEvent(id, authHeader);
    }
}
