package com.tickets.bff.service;

import com.tickets.bff.client.TicketSalesClient;
import com.tickets.bff.dto.TicketSaleRequestDto;
import com.tickets.bff.dto.TicketSaleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketSalesBffService {

    private final TicketSalesClient ticketSalesClient;

    public ResponseEntity<List<TicketSaleResponseDto>> getAllSales(String authHeader) {
        return ticketSalesClient.getAllSales(authHeader);
    }

    public ResponseEntity<TicketSaleResponseDto> getSaleById(UUID id, String authHeader) {
        return ticketSalesClient.getSaleById(id, authHeader);
    }

    public ResponseEntity<TicketSaleResponseDto> createSale(TicketSaleRequestDto dto, String authHeader) {
        return ticketSalesClient.createSale(dto, authHeader);
    }

    public ResponseEntity<Void> deleteSale(UUID id, String authHeader) {
        return ticketSalesClient.deleteSale(id, authHeader);
    }
}
