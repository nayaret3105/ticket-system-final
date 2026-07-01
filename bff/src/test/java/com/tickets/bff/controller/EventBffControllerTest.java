package com.tickets.bff.controller;

import com.tickets.bff.dto.EventRequestDto;
import com.tickets.bff.dto.EventResponseDto;
import com.tickets.bff.dto.MessageResponseDto;
import com.tickets.bff.service.EventBffService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventBffControllerTest {

    @Mock
    private EventBffService eventBffService;

    private static final String AUTH_HEADER = "Bearer test-token";

    private EventResponseDto buildResponse(UUID id) {
        return new EventResponseDto(id, "Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"), LocalDateTime.now());
    }

    @Test
    void listarShouldReturnOk() {
        EventBffController controller = new EventBffController(eventBffService);
        when(eventBffService.getAllEvents(AUTH_HEADER)).thenReturn(ResponseEntity.ok(List.of(buildResponse(UUID.randomUUID()))));

        ResponseEntity<List<EventResponseDto>> result = controller.listar(AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void buscarPorIdShouldReturnOk() {
        EventBffController controller = new EventBffController(eventBffService);
        UUID id = UUID.randomUUID();
        when(eventBffService.getEventById(id, AUTH_HEADER)).thenReturn(ResponseEntity.ok(buildResponse(id)));

        ResponseEntity<EventResponseDto> result = controller.buscarPorId(id, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void crearShouldReturnCreated() {
        EventBffController controller = new EventBffController(eventBffService);
        EventRequestDto request = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"));
        when(eventBffService.createEvent(any(), any())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(UUID.randomUUID())));

        ResponseEntity<EventResponseDto> result = controller.crear(request, AUTH_HEADER);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void actualizarShouldReturnOk() {
        EventBffController controller = new EventBffController(eventBffService);
        UUID id = UUID.randomUUID();
        EventRequestDto request = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"));
        when(eventBffService.updateEvent(any(), any(), any())).thenReturn(ResponseEntity.ok(buildResponse(id)));

        ResponseEntity<EventResponseDto> result = controller.actualizar(id, request, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void eliminarShouldReturnOk() {
        EventBffController controller = new EventBffController(eventBffService);
        UUID id = UUID.randomUUID();
        when(eventBffService.deleteEvent(id, AUTH_HEADER)).thenReturn(ResponseEntity.ok(new MessageResponseDto("Evento eliminado")));

        ResponseEntity<MessageResponseDto> result = controller.eliminar(id, AUTH_HEADER);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
