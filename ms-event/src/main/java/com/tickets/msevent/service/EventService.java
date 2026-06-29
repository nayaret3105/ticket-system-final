package com.tickets.msevent.service;

import com.tickets.msevent.dto.request.EventRequestDto;
import com.tickets.msevent.dto.response.EventResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Contract for CRUD operations on events.
 */
public interface EventService {

    /**
     * Creates a new event.
     *
     * @param request the event data
     * @return the persisted event
     * @throws com.tickets.msevent.exception.EventAlreadyExistsException if an event with the same name and date exists
     */
    EventResponseDto createEvent(EventRequestDto request);

    /**
     * Retrieves an event by its identifier.
     *
     * @param id the event UUID
     * @return the event data
     * @throws com.tickets.msevent.exception.EventNotFoundException if no event with the given id exists
     */
    EventResponseDto getEventById(UUID id);

    /**
     * Returns all events ordered by date ascending.
     *
     * @return list of all events
     */
    List<EventResponseDto> getAllEvents();

    /**
     * Replaces all fields of an existing event.
     *
     * @param id      the event to update
     * @param request the new event data
     * @return the updated event
     * @throws com.tickets.msevent.exception.EventNotFoundException if no event with the given id exists
     */
    EventResponseDto updateEvent(UUID id, EventRequestDto request);

    /**
     * Permanently removes an event.
     *
     * @param id the event to delete
     * @throws com.tickets.msevent.exception.EventNotFoundException if no event with the given id exists
     */
    void deleteEvent(UUID id);
}
