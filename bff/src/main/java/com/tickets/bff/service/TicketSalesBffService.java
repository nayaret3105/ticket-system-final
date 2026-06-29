package com.tickets.bff.service;

import com.tickets.bff.client.TicketSalesClient;
import com.tickets.bff.dto.TicketSaleRequestDto;
import com.tickets.bff.dto.TicketSaleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * BFF service that delegates ticket sale requests to ms-ticket-sales.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TicketSalesBffService {

    private final TicketSalesClient ticketSalesClient;

    public ResponseEntity<List<TicketSaleResponseDto>> getAllSales(String authHeader) {
        log.debug("BFF: delegando listado de ventas");
        return ticketSalesClient.getAllSales(authHeader);
    }

    public ResponseEntity<TicketSaleResponseDto> getSaleById(UUID id, String authHeader) {
        log.debug("BFF: delegando busqueda de venta {}", id);
        return ticketSalesClient.getSaleById(id, authHeader);
    }

    public ResponseEntity<TicketSaleResponseDto> createSale(TicketSaleRequestDto request, String authHeader) {
        log.debug("BFF: delegando registro de venta para evento {}", request.eventId());
        return ticketSalesClient.createSale(request, authHeader);
    }

    public ResponseEntity<Void> deleteSale(UUID id, String authHeader) {
        log.debug("BFF: delegando eliminacion de venta {}", id);
        return ticketSalesClient.deleteSale(id, authHeader);
    }
}
