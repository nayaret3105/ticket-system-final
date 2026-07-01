package com.tickets.msevent.controller;

import com.tickets.msevent.dto.EventRequestDto;
import com.tickets.msevent.dto.EventResponseDto;
import com.tickets.msevent.dto.MessageResponseDto;
import com.tickets.msevent.service.EventService;
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
class EventControllerTest {

    @Mock
    private EventService eventService;

    private EventResponseDto buildResponse(UUID id) {
        return new EventResponseDto(id, "Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"), LocalDateTime.now());
    }

    @Test
    void listarShouldReturnOk() {
        EventController controller = new EventController(eventService);
        when(eventService.listarTodos()).thenReturn(List.of(buildResponse(UUID.randomUUID())));

        ResponseEntity<List<EventResponseDto>> result = controller.listar();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void buscarPorIdShouldReturnOk() {
        EventController controller = new EventController(eventService);
        UUID id = UUID.randomUUID();
        when(eventService.buscarPorId(id)).thenReturn(buildResponse(id));

        ResponseEntity<EventResponseDto> result = controller.buscarPorId(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void crearShouldReturnCreated() {
        EventController controller = new EventController(eventService);
        EventRequestDto request = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"));
        when(eventService.crear(any())).thenReturn(buildResponse(UUID.randomUUID()));

        ResponseEntity<EventResponseDto> result = controller.crear(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void actualizarShouldReturnOk() {
        EventController controller = new EventController(eventService);
        UUID id = UUID.randomUUID();
        EventRequestDto request = new EventRequestDto("Concierto Rock", "Descripcion", LocalDateTime.now(),
                "Estadio Nacional", 5000, new BigDecimal("25000.00"));
        when(eventService.actualizar(any(), any())).thenReturn(buildResponse(id));

        ResponseEntity<EventResponseDto> result = controller.actualizar(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void eliminarShouldReturnOk() {
        EventController controller = new EventController(eventService);
        UUID id = UUID.randomUUID();
        when(eventService.eliminar(id)).thenReturn(new MessageResponseDto("Evento eliminado: Concierto Rock"));

        ResponseEntity<MessageResponseDto> result = controller.eliminar(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
}
