package com.tickets.mssales.controller;

import com.tickets.mssales.dto.MessageResponseDto;
import com.tickets.mssales.dto.TicketSaleRequestDto;
import com.tickets.mssales.dto.TicketSaleResponseDto;
import com.tickets.mssales.service.TicketSaleService;
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
@RequestMapping("/sales")
@RequiredArgsConstructor
@Tag(name = "Ticket Sales", description = "Endpoints for ticket sale management")
public class TicketSaleController {

    private final TicketSaleService ticketSaleService;

    @Operation(summary = "List all sales", description = "Returns the complete history of registered ticket sales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TicketSaleResponseDto>> listar() {
        return ResponseEntity.ok(ticketSaleService.listarTodas());
    }

    @Operation(summary = "Find sale by ID", description = "Returns the complete data of a specific ticket sale.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale found"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketSaleResponseDto> buscarPorId(
            @Parameter(description = "Sale unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(ticketSaleService.buscarPorId(id));
    }

    @Operation(
            summary = "Register ticket sale",
            description = "Registers the purchase of tickets for an event. Validates event existence and calculates total automatically.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Ticket sale data",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or event does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TicketSaleResponseDto> registrar(
            @Valid @RequestBody TicketSaleRequestDto dto,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketSaleService.registrar(dto, authHeader));
    }

    @Operation(summary = "Delete sale", description = "Permanently removes a ticket sale record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> eliminar(
            @Parameter(description = "Sale unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(ticketSaleService.eliminar(id));
    }
}
