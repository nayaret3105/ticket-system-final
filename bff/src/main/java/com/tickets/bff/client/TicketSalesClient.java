package com.tickets.bff.client;

import com.tickets.bff.dto.TicketSaleRequestDto;
import com.tickets.bff.dto.TicketSaleResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Component
public class TicketSalesClient {

    private final RestClient restClient;

    public TicketSalesClient(@Qualifier("ticketSalesRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<List<TicketSaleResponseDto>> getAllSales(String authHeader) {
        return restClient.get()
                .uri("/sales")
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<TicketSaleResponseDto>>() {});
    }

    public ResponseEntity<TicketSaleResponseDto> getSaleById(UUID id, String authHeader) {
        return restClient.get()
                .uri("/sales/{id}", id)
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(TicketSaleResponseDto.class);
    }

    public ResponseEntity<TicketSaleResponseDto> createSale(TicketSaleRequestDto dto, String authHeader) {
        return restClient.post()
                .uri("/sales")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toEntity(TicketSaleResponseDto.class);
    }

    public ResponseEntity<Void> deleteSale(UUID id, String authHeader) {
        return restClient.delete()
                .uri("/sales/{id}", id)
                .header("Authorization", authHeader)
                .retrieve()
                .toBodilessEntity();
    }
}
