package com.tickets.mssales.controller;

import com.tickets.mssales.dto.request.TicketSaleRequestDto;
import com.tickets.mssales.dto.response.TicketSaleResponseDto;
import com.tickets.mssales.service.TicketSaleService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@Tag(name = "Ventas de Entradas", description = "Registro y gestion de ventas de entradas a eventos")
@SecurityRequirement(name = "Bearer")
public class TicketSaleController {

    private final TicketSaleService ticketSaleService;

    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Retorna el historial completo de ventas registradas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<TicketSaleResponseDto>> getAll() {
        return ResponseEntity.ok(ticketSaleService.getAllSales());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID", description = "Retorna los datos completos de una venta especifica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta encontrada"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TicketSaleResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketSaleService.getSaleById(id));
    }

    @PostMapping
    @Operation(
        summary = "Registrar venta de entradas",
        description = "Registra la compra de entradas. Valida existencia del evento en ms-event y calcula el total automaticamente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos o el evento no existe"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TicketSaleResponseDto> create(@Valid @RequestBody TicketSaleRequestDto request,
                                                        @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketSaleService.createSale(request, authHeader));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta", description = "Elimina permanentemente un registro de venta")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Venta eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        ticketSaleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
