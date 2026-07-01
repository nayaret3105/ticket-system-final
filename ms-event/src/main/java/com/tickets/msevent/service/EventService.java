package com.tickets.msevent.service;

import com.tickets.msevent.dto.EventRequestDto;
import com.tickets.msevent.dto.EventResponseDto;
import com.tickets.msevent.dto.MessageResponseDto;
import java.util.List;
import java.util.UUID;

public interface EventService {
    List<EventResponseDto> listarTodos();
    EventResponseDto buscarPorId(UUID id);
    EventResponseDto crear(EventRequestDto dto);
    EventResponseDto actualizar(UUID id, EventRequestDto dto);
    MessageResponseDto eliminar(UUID id);
}
