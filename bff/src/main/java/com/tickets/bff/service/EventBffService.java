package com.tickets.bff.service;

import com.tickets.bff.client.EventClient;
import com.tickets.bff.dto.EventRequestDto;
import com.tickets.bff.dto.EventResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * BFF service that delegates event requests to ms-event.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventBffService {

    private final EventClient eventClient;

    public ResponseEntity<List<EventResponseDto>> getAllEvents(String authHeader) {
        log.debug("BFF: delegando listado de eventos");
        return eventClient.getAllEvents(authHeader);
    }

    public ResponseEntity<EventResponseDto> getEventById(UUID id, String authHeader) {
        log.debug("BFF: delegando busqueda de evento {}", id);
        return eventClient.getEventById(id, authHeader);
    }

    public ResponseEntity<EventResponseDto> createEvent(EventRequestDto request, String authHeader) {
        log.debug("BFF: delegando creacion de evento '{}'", request.name());
        return eventClient.createEvent(request, authHeader);
    }

    public ResponseEntity<EventResponseDto> updateEvent(UUID id, EventRequestDto request, String authHeader) {
        log.debug("BFF: delegando actualizacion de evento {}", id);
        return eventClient.updateEvent(id, request, authHeader);
    }

    public ResponseEntity<Void> deleteEvent(UUID id, String authHeader) {
        log.debug("BFF: delegando eliminacion de evento {}", id);
        return eventClient.deleteEvent(id, authHeader);
    }
}
