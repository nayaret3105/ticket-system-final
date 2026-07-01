package com.tickets.msevent.service;

import com.tickets.msevent.dto.EventRequestDto;
import com.tickets.msevent.dto.EventResponseDto;
import com.tickets.msevent.dto.MessageResponseDto;
import com.tickets.msevent.exception.EventAlreadyExistsException;
import com.tickets.msevent.exception.EventNotFoundException;
import com.tickets.msevent.model.Event;
import com.tickets.msevent.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private EventResponseDto toDto(Event e) {
        return new EventResponseDto(e.getId(), e.getName(), e.getDescription(),
                e.getEventDate(), e.getLocation(), e.getCapacity(),
                e.getTicketPrice(), e.getCreatedAt());
    }

    @Override
    public List<EventResponseDto> listarTodos() {
        return eventRepository.findAllByOrderByEventDateAsc().stream().map(this::toDto).toList();
    }

    @Override
    public EventResponseDto buscarPorId(UUID id) {
        return toDto(eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado: " + id)));
    }

    @Override
    public EventResponseDto crear(EventRequestDto dto) {
        if (eventRepository.existsByNameAndEventDate(dto.name(), dto.eventDate())) {
            throw new EventAlreadyExistsException(
                    "Ya existe un evento con el nombre '" + dto.name() + "' en la misma fecha");
        }
        Event saved = eventRepository.save(Event.builder()
                .name(dto.name())
                .description(dto.description())
                .eventDate(dto.eventDate())
                .location(dto.location())
                .capacity(dto.capacity())
                .ticketPrice(dto.ticketPrice())
                .build());
        return toDto(saved);
    }

    @Override
    public EventResponseDto actualizar(UUID id, EventRequestDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado: " + id));
        event.setName(dto.name());
        event.setDescription(dto.description());
        event.setEventDate(dto.eventDate());
        event.setLocation(dto.location());
        event.setCapacity(dto.capacity());
        event.setTicketPrice(dto.ticketPrice());
        return toDto(eventRepository.save(event));
    }

    @Override
    public MessageResponseDto eliminar(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado: " + id));
        eventRepository.deleteById(id);
        return new MessageResponseDto("Evento eliminado: " + event.getName());
    }
}
