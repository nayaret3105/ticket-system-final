package com.tickets.bff.controller;

import com.tickets.bff.dto.TicketSaleRequestDto;
import com.tickets.bff.dto.TicketSaleResponseDto;
import com.tickets.bff.service.TicketSalesBffService;
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
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Ticket Sales", description = "Proxy for ticket sales management towards ms-ticket-sales")
public class TicketSalesBffController {

    private final TicketSalesBffService ticketSalesBffService;

    @Operation(summary = "List all sales", description = "Returns the complete history of ticket sales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TicketSaleResponseDto>> listar(@RequestHeader("Authorization") String authHeader) {
        return ticketSalesBffService.getAllSales(authHeader);
    }

    @Operation(summary = "Find sale by ID", description = "Returns data of a specific ticket sale.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale found"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketSaleResponseDto> buscarPorId(
            @Parameter(description = "Sale unique identifier", required = true) @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        return ticketSalesBffService.getSaleById(id, authHeader);
    }

    @Operation(
            summary = "Register ticket sale",
            description = "Registers the purchase of tickets for an event.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Ticket sale data", required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or event does not exist"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TicketSaleResponseDto> registrar(
            @Valid @RequestBody TicketSaleRequestDto dto,
            @RequestHeader("Authorization") String authHeader) {
        return ticketSalesBffService.createSale(dto, authHeader);
    }

    @Operation(summary = "Delete sale", description = "Permanently removes a ticket sale record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Sale unique identifier", required = true) @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        return ticketSalesBffService.deleteSale(id, authHeader);
    }
}
