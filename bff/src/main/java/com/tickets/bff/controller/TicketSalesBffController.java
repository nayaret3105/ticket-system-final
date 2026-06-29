package com.tickets.bff.controller;

import com.tickets.bff.dto.TicketSaleRequestDto;
import com.tickets.bff.dto.TicketSaleResponseDto;
import com.tickets.bff.service.TicketSalesBffService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Ventas BFF", description = "Proxy de venta de entradas hacia ms-ticket-sales")
@SecurityRequirement(name = "Bearer")
public class TicketSalesBffController {

    private final TicketSalesBffService ticketSalesBffService;

    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Retorna el historial completo de ventas de entradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<TicketSaleResponseDto>> getAll(@RequestHeader("Authorization") String authHeader) {
        return ticketSalesBffService.getAllSales(authHeader);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID", description = "Retorna los datos de una venta especifica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta encontrada"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TicketSaleResponseDto> getById(@PathVariable UUID id,
                                                         @RequestHeader("Authorization") String authHeader) {
        return ticketSalesBffService.getSaleById(id, authHeader);
    }

    @PostMapping
    @Operation(summary = "Registrar venta de entradas", description = "Registra la compra de entradas para un evento. El total se calcula automaticamente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos o el evento no existe"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TicketSaleResponseDto> create(@Valid @RequestBody TicketSaleRequestDto request,
                                                        @RequestHeader("Authorization") String authHeader) {
        return ticketSalesBffService.createSale(request, authHeader);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta", description = "Elimina un registro de venta del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Venta eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @RequestHeader("Authorization") String authHeader) {
        return ticketSalesBffService.deleteSale(id, authHeader);
    }
}
