package com.tickets.bff.client;

import com.tickets.bff.dto.TicketSaleRequestDto;
import com.tickets.bff.dto.TicketSaleResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class TicketSalesClient {

    private final RestClient restClient;

    public TicketSalesClient(@Qualifier("ticketSalesRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<List<TicketSaleResponseDto>> getAllSales(String authHeader) {
        log.debug("Llamando a ms-ticket-sales: GET /sales");
        return restClient.get()
                .uri("/sales")
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<TicketSaleResponseDto> getSaleById(UUID id, String authHeader) {
        log.debug("Llamando a ms-ticket-sales: GET /sales/{}", id);
        return restClient.get()
                .uri("/sales/{id}", id)
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(TicketSaleResponseDto.class);
    }

    public ResponseEntity<TicketSaleResponseDto> createSale(TicketSaleRequestDto request, String authHeader) {
        log.debug("Llamando a ms-ticket-sales: POST /sales");
        return restClient.post()
                .uri("/sales")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(TicketSaleResponseDto.class);
    }

    public ResponseEntity<Void> deleteSale(UUID id, String authHeader) {
        log.debug("Llamando a ms-ticket-sales: DELETE /sales/{}", id);
        return restClient.delete()
                .uri("/sales/{id}", id)
                .header("Authorization", authHeader)
                .retrieve()
                .toBodilessEntity();
    }
}
