package com.tickets.mssales.client;

import com.tickets.mssales.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Component
public class EventClient {

    private final RestClient restClient;

    public EventClient(@Value("${event.service.url}") String msEventUrl) {
        this.restClient = RestClient.builder().baseUrl(msEventUrl).build();
    }

    public BigDecimal getEventPrice(UUID eventId, String authHeader) {
        try {
            Map<?, ?> event = restClient.get()
                    .uri("/events/{id}", eventId)
                    .header("Authorization", authHeader)
                    .retrieve()
                    .body(Map.class);

            if (event == null || event.get("ticketPrice") == null) {
                throw new BusinessException("No se pudo obtener el precio del evento: " + eventId);
            }
            return new BigDecimal(event.get("ticketPrice").toString());
        } catch (HttpClientErrorException.NotFound e) {
            throw new BusinessException("El evento con id " + eventId + " no existe");
        } catch (BusinessException e) {
            throw e;
        } catch (ResourceAccessException e) {
            throw new BusinessException("El servicio de eventos no esta disponible");
        } catch (Exception e) {
            throw new BusinessException("Error al consultar el servicio de eventos");
        }
    }
}
