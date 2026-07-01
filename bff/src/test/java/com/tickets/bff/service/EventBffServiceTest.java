package com.tickets.bff.service;

import com.tickets.bff.client.EventClient;
import com.tickets.bff.dto.EventRequestDto;
import com.tickets.bff.dto.EventResponseDto;
import com.tickets.bff.dto.MessageResponseDto;
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
class EventBffServiceTest {

    @Mock
    private EventClient eventClient;

    private static final String AUTH_HEADER = "Bearer test-token";

    private EventResponseDto buildResponse(UUID id) {
        return new EventResponseDto(id, "Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"), LocalDateTime.now());
    }

    @Test
    void getAllEventsShouldDelegateToClient() {
        EventBffService service = new EventBffService(eventClient);
        when(eventClient.getAllEvents(AUTH_HEADER)).thenReturn(ResponseEntity.ok(List.of(buildResponse(UUID.randomUUID()))));

        ResponseEntity<List<EventResponseDto>> result = service.getAllEvents(AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void getEventByIdShouldDelegateToClient() {
        EventBffService service = new EventBffService(eventClient);
        UUID id = UUID.randomUUID();
        when(eventClient.getEventById(id, AUTH_HEADER)).thenReturn(ResponseEntity.ok(buildResponse(id)));

        ResponseEntity<EventResponseDto> result = service.getEventById(id, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void createEventShouldDelegateToClient() {
        EventBffService service = new EventBffService(eventClient);
        EventRequestDto request = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"));
        ResponseEntity<EventResponseDto> response = ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(UUID.randomUUID()));
        when(eventClient.createEvent(request, AUTH_HEADER)).thenReturn(response);

        ResponseEntity<EventResponseDto> result = service.createEvent(request, AUTH_HEADER);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void updateEventShouldDelegateToClient() {
        EventBffService service = new EventBffService(eventClient);
        UUID id = UUID.randomUUID();
        EventRequestDto request = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"));
        when(eventClient.updateEvent(id, request, AUTH_HEADER)).thenReturn(ResponseEntity.ok(buildResponse(id)));

        ResponseEntity<EventResponseDto> result = service.updateEvent(id, request, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void deleteEventShouldDelegateToClient() {
        EventBffService service = new EventBffService(eventClient);
        UUID id = UUID.randomUUID();
        when(eventClient.deleteEvent(id, AUTH_HEADER)).thenReturn(ResponseEntity.ok(new MessageResponseDto("Evento eliminado: Concierto Rock")));

        ResponseEntity<MessageResponseDto> result = service.deleteEvent(id, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
