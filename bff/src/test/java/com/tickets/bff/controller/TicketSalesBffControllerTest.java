package com.tickets.bff.controller;

import com.tickets.bff.dto.TicketSaleRequestDto;
import com.tickets.bff.dto.TicketSaleResponseDto;
import com.tickets.bff.service.TicketSalesBffService;
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
class TicketSalesBffControllerTest {

    @Mock
    private TicketSalesBffService ticketSalesBffService;

    private static final String AUTH_HEADER = "Bearer test-token";

    private TicketSaleResponseDto buildResponse(UUID id, UUID eventId) {
        return new TicketSaleResponseDto(id, eventId, "Juan Perez", 2, new BigDecimal("50000"), LocalDateTime.now());
    }

    @Test
    void listarShouldReturnOk() {
        TicketSalesBffController controller = new TicketSalesBffController(ticketSalesBffService);
        when(ticketSalesBffService.getAllSales(AUTH_HEADER))
                .thenReturn(ResponseEntity.ok(List.of(buildResponse(UUID.randomUUID(), UUID.randomUUID()))));

        ResponseEntity<List<TicketSaleResponseDto>> result = controller.listar(AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void buscarPorIdShouldReturnOk() {
        TicketSalesBffController controller = new TicketSalesBffController(ticketSalesBffService);
        UUID id = UUID.randomUUID();
        when(ticketSalesBffService.getSaleById(id, AUTH_HEADER)).thenReturn(ResponseEntity.ok(buildResponse(id, UUID.randomUUID())));

        ResponseEntity<TicketSaleResponseDto> result = controller.buscarPorId(id, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void registrarShouldReturnCreated() {
        TicketSalesBffController controller = new TicketSalesBffController(ticketSalesBffService);
        UUID eventId = UUID.randomUUID();
        TicketSaleRequestDto request = new TicketSaleRequestDto(eventId, "Juan Perez", 2);
        when(ticketSalesBffService.createSale(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(UUID.randomUUID(), eventId)));

        ResponseEntity<TicketSaleResponseDto> result = controller.registrar(request, AUTH_HEADER);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void eliminarShouldReturnOk() {
        TicketSalesBffController controller = new TicketSalesBffController(ticketSalesBffService);
        UUID id = UUID.randomUUID();
        when(ticketSalesBffService.deleteSale(id, AUTH_HEADER)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> result = controller.eliminar(id, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
