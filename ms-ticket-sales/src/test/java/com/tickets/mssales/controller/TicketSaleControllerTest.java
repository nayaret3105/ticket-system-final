package com.tickets.mssales.controller;

import com.tickets.mssales.dto.MessageResponseDto;
import com.tickets.mssales.dto.TicketSaleRequestDto;
import com.tickets.mssales.dto.TicketSaleResponseDto;
import com.tickets.mssales.service.TicketSaleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketSaleControllerTest {

    @Mock
    private TicketSaleService ticketSaleService;

    private static final String AUTH_HEADER = "Bearer test-token";

    private TicketSaleResponseDto buildResponse(UUID id, UUID eventId) {
        return new TicketSaleResponseDto(id, eventId, "Juan Perez", 2, new BigDecimal("50000"), LocalDateTime.now());
    }

    @Test
    void listarShouldReturnOk() {
        TicketSaleController controller = new TicketSaleController(ticketSaleService);
        when(ticketSaleService.listarTodas()).thenReturn(List.of(buildResponse(UUID.randomUUID(), UUID.randomUUID())));

        ResponseEntity<List<TicketSaleResponseDto>> result = controller.listar();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void buscarPorIdShouldReturnOk() {
        TicketSaleController controller = new TicketSaleController(ticketSaleService);
        UUID id = UUID.randomUUID();
        when(ticketSaleService.buscarPorId(id)).thenReturn(buildResponse(id, UUID.randomUUID()));

        ResponseEntity<TicketSaleResponseDto> result = controller.buscarPorId(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void registrarShouldReturnCreated() {
        TicketSaleController controller = new TicketSaleController(ticketSaleService);
        UUID eventId = UUID.randomUUID();
        TicketSaleRequestDto request = new TicketSaleRequestDto(eventId, "Juan Perez", 2);
        when(ticketSaleService.registrar(any(), any())).thenReturn(buildResponse(UUID.randomUUID(), eventId));

        ResponseEntity<TicketSaleResponseDto> result = controller.registrar(request, AUTH_HEADER);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void eliminarShouldReturnOk() {
        TicketSaleController controller = new TicketSaleController(ticketSaleService);
        UUID id = UUID.randomUUID();
        when(ticketSaleService.eliminar(id)).thenReturn(new MessageResponseDto("Venta eliminada: " + id));

        ResponseEntity<MessageResponseDto> result = controller.eliminar(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
}
