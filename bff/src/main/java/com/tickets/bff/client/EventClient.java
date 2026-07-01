package com.tickets.bff.client;

import com.tickets.bff.dto.EventRequestDto;
import com.tickets.bff.dto.EventResponseDto;
import com.tickets.bff.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Component
public class EventClient {

    private final RestClient restClient;

    public EventClient(@Qualifier("eventRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<List<EventResponseDto>> getAllEvents(String authHeader) {
        return restClient.get()
                .uri("/events")
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<EventResponseDto>>() {});
    }

    public ResponseEntity<EventResponseDto> getEventById(UUID id, String authHeader) {
        return restClient.get()
                .uri("/events/{id}", id)
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(EventResponseDto.class);
    }

    public ResponseEntity<EventResponseDto> createEvent(EventRequestDto dto, String authHeader) {
        return restClient.post()
                .uri("/events")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toEntity(EventResponseDto.class);
    }

    public ResponseEntity<EventResponseDto> updateEvent(UUID id, EventRequestDto dto, String authHeader) {
        return restClient.put()
                .uri("/events/{id}", id)
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toEntity(EventResponseDto.class);
    }

    public ResponseEntity<MessageResponseDto> deleteEvent(UUID id, String authHeader) {
        return restClient.delete()
                .uri("/events/{id}", id)
                .header("Authorization", authHeader)
                .retrieve()
                .toEntity(MessageResponseDto.class);
    }
}
