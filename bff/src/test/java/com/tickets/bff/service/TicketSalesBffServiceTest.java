package com.tickets.bff.service;

import com.tickets.bff.client.TicketSalesClient;
import com.tickets.bff.dto.TicketSaleRequestDto;
import com.tickets.bff.dto.TicketSaleResponseDto;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketSalesBffServiceTest {

    @Mock
    private TicketSalesClient ticketSalesClient;

    private static final String AUTH_HEADER = "Bearer test-token";

    private TicketSaleResponseDto buildResponse(UUID id, UUID eventId) {
        return new TicketSaleResponseDto(id, eventId, "Juan Perez", 2, new BigDecimal("50000"), LocalDateTime.now());
    }

    @Test
    void getAllSalesShouldDelegateToClient() {
        TicketSalesBffService service = new TicketSalesBffService(ticketSalesClient);
        when(ticketSalesClient.getAllSales(AUTH_HEADER))
                .thenReturn(ResponseEntity.ok(List.of(buildResponse(UUID.randomUUID(), UUID.randomUUID()))));

        ResponseEntity<List<TicketSaleResponseDto>> result = service.getAllSales(AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void getSaleByIdShouldDelegateToClient() {
        TicketSalesBffService service = new TicketSalesBffService(ticketSalesClient);
        UUID id = UUID.randomUUID();
        when(ticketSalesClient.getSaleById(id, AUTH_HEADER)).thenReturn(ResponseEntity.ok(buildResponse(id, UUID.randomUUID())));

        ResponseEntity<TicketSaleResponseDto> result = service.getSaleById(id, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void createSaleShouldDelegateToClient() {
        TicketSalesBffService service = new TicketSalesBffService(ticketSalesClient);
        UUID eventId = UUID.randomUUID();
        TicketSaleRequestDto request = new TicketSaleRequestDto(eventId, "Juan Perez", 2);
        ResponseEntity<TicketSaleResponseDto> response = ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(UUID.randomUUID(), eventId));
        when(ticketSalesClient.createSale(request, AUTH_HEADER)).thenReturn(response);

        ResponseEntity<TicketSaleResponseDto> result = service.createSale(request, AUTH_HEADER);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void deleteSaleShouldDelegateToClient() {
        TicketSalesBffService service = new TicketSalesBffService(ticketSalesClient);
        UUID id = UUID.randomUUID();
        when(ticketSalesClient.deleteSale(id, AUTH_HEADER)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> result = service.deleteSale(id, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
