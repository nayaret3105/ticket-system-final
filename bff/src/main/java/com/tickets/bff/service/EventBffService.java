package com.tickets.bff.service;

import com.tickets.bff.client.EventClient;
import com.tickets.bff.dto.EventRequestDto;
import com.tickets.bff.dto.EventResponseDto;
import com.tickets.bff.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventBffService {

    private final EventClient eventClient;

    public ResponseEntity<List<EventResponseDto>> getAllEvents(String authHeader) {
        return eventClient.getAllEvents(authHeader);
    }

    public ResponseEntity<EventResponseDto> getEventById(UUID id, String authHeader) {
        return eventClient.getEventById(id, authHeader);
    }

    public ResponseEntity<EventResponseDto> createEvent(EventRequestDto dto, String authHeader) {
        return eventClient.createEvent(dto, authHeader);
    }

    public ResponseEntity<EventResponseDto> updateEvent(UUID id, EventRequestDto dto, String authHeader) {
        return eventClient.updateEvent(id, dto, authHeader);
    }

    public ResponseEntity<MessageResponseDto> deleteEvent(UUID id, String authHeader) {
        return eventClient.deleteEvent(id, authHeader);
    }
}
