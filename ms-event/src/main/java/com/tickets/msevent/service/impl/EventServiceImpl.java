package com.tickets.msevent.service.impl;

import com.tickets.msevent.dto.request.EventRequestDto;
import com.tickets.msevent.dto.response.EventResponseDto;
import com.tickets.msevent.entity.Event;
import com.tickets.msevent.exception.EventAlreadyExistsException;
import com.tickets.msevent.exception.EventNotFoundException;
import com.tickets.msevent.repository.EventRepository;
import com.tickets.msevent.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Default implementation of {@link EventService}.
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    /** {@inheritDoc} */
    @Override
    public EventResponseDto createEvent(EventRequestDto request) {
        if (eventRepository.existsByNameAndEventDate(request.name(), request.eventDate())) {
            throw new EventAlreadyExistsException(
                    "Ya existe un evento con el nombre '" + request.name() + "' en la misma fecha");
        }
        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .eventDate(request.eventDate())
                .location(request.location())
                .capacity(request.capacity())
                .ticketPrice(request.ticketPrice())
                .build();
        return toResponse(eventRepository.save(event));
    }

    /** {@inheritDoc} */
    @Override
    public EventResponseDto getEventById(UUID id) {
        return toResponse(findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public EventResponseDto updateEvent(UUID id, EventRequestDto request) {
        Event event = findById(id);
        event.setName(request.name());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setLocation(request.location());
        event.setCapacity(request.capacity());
        event.setTicketPrice(request.ticketPrice());
        return toResponse(eventRepository.save(event));
    }

    /** {@inheritDoc} */
    @Override
    public void deleteEvent(UUID id) {
        findById(id);
        eventRepository.deleteById(id);
    }

    private Event findById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con id: " + id));
    }

    private EventResponseDto toResponse(Event event) {
        return new EventResponseDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getLocation(),
                event.getCapacity(),
                event.getTicketPrice(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}
