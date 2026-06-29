package com.tickets.bff.client;

import com.tickets.bff.dto.EventRequestDto;
import com.tickets.bff.dto.EventResponseDto;
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
public class EventClient {

    private final RestClient restClient;

    public EventClient(@Qualifier("eventRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<List<EventResponseDto>> getAllEvents(String authHeader) {
        log.debug("Llamando a ms-event: GET /events");
        return restClient.get()
                .uri("/events")
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<EventResponseDto> getEventById(UUID id, String authHeader) {
        log.debug("Llamando a ms-event: GET /events/{}", id);
        return restClient.get()
                .uri("/events/{id}", id)
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(EventResponseDto.class);
    }

    public ResponseEntity<EventResponseDto> createEvent(EventRequestDto request, String authHeader) {
        log.debug("Llamando a ms-event: POST /events");
        return restClient.post()
                .uri("/events")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(EventResponseDto.class);
    }

    public ResponseEntity<EventResponseDto> updateEvent(UUID id, EventRequestDto request, String authHeader) {
        log.debug("Llamando a ms-event: PUT /events/{}", id);
        return restClient.put()
                .uri("/events/{id}", id)
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(EventResponseDto.class);
    }

    public ResponseEntity<Void> deleteEvent(UUID id, String authHeader) {
        log.debug("Llamando a ms-event: DELETE /events/{}", id);
        return restClient.delete()
                .uri("/events/{id}", id)
                .header("Authorization", authHeader)
                .retrieve()
                .toBodilessEntity();
    }
}
