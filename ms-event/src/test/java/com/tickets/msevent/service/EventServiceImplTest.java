package com.tickets.msevent.service;

import com.tickets.msevent.dto.request.EventRequestDto;
import com.tickets.msevent.dto.response.EventResponseDto;
import com.tickets.msevent.entity.Event;
import com.tickets.msevent.exception.EventAlreadyExistsException;
import com.tickets.msevent.exception.EventNotFoundException;
import com.tickets.msevent.repository.EventRepository;
import com.tickets.msevent.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private final LocalDateTime EVENT_DATE = LocalDateTime.of(2026, 9, 15, 20, 0);

    private EventRequestDto buildRequest() {
        return new EventRequestDto(
                "Rock Concert",
                "Live band performance",
                EVENT_DATE,
                "Municipal Theatre",
                500,
                new BigDecimal("25000")
        );
    }

    private Event buildEvent(UUID id) {
        return Event.builder()
                .id(id)
                .name("Rock Concert")
                .description("Live band performance")
                .eventDate(EVENT_DATE)
                .location("Municipal Theatre")
                .capacity(500)
                .ticketPrice(new BigDecimal("25000"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createEvent_whenEventDoesNotExist_shouldPersistAndReturnDto() {
        UUID id = UUID.randomUUID();
        Event saved = buildEvent(id);

        when(eventRepository.existsByNameAndEventDate("Rock Concert", EVENT_DATE)).thenReturn(false);
        when(eventRepository.save(any(Event.class))).thenReturn(saved);

        EventResponseDto response = eventService.createEvent(buildRequest());

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("Rock Concert");
        assertThat(response.capacity()).isEqualTo(500);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void createEvent_whenEventWithSameNameAndDateExists_shouldThrowEventAlreadyExistsException() {
        when(eventRepository.existsByNameAndEventDate("Rock Concert", EVENT_DATE)).thenReturn(true);

        assertThatThrownBy(() -> eventService.createEvent(buildRequest()))
                .isInstanceOf(EventAlreadyExistsException.class)
                .hasMessageContaining("Rock Concert");

        verify(eventRepository, never()).save(any());
    }

    @Test
    void getEventById_whenEventExists_shouldReturnEventResponseDto() {
        UUID id = UUID.randomUUID();

        when(eventRepository.findById(id)).thenReturn(Optional.of(buildEvent(id)));

        EventResponseDto response = eventService.getEventById(id);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.location()).isEqualTo("Municipal Theatre");
        assertThat(response.ticketPrice()).isEqualByComparingTo("25000");
    }

    @Test
    void getEventById_whenEventDoesNotExist_shouldThrowEventNotFoundException() {
        UUID id = UUID.randomUUID();

        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.getEventById(id))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void getAllEvents_shouldReturnListOrderedByDate() {
        List<Event> events = List.of(buildEvent(UUID.randomUUID()), buildEvent(UUID.randomUUID()));

        when(eventRepository.findAllByOrderByEventDateAsc()).thenReturn(events);

        assertThat(eventService.getAllEvents()).hasSize(2);
    }

    @Test
    void getAllEvents_whenNoEventsExist_shouldReturnEmptyList() {
        when(eventRepository.findAllByOrderByEventDateAsc()).thenReturn(List.of());

        assertThat(eventService.getAllEvents()).isEmpty();
    }

    @Test
    void updateEvent_whenEventExists_shouldUpdateFieldsAndReturnDto() {
        UUID id = UUID.randomUUID();
        Event existing = buildEvent(id);
        EventRequestDto request = new EventRequestDto(
                "Jazz Concert", "Smooth jazz", EVENT_DATE, "Municipal Theatre", 200, new BigDecimal("25000"));

        when(eventRepository.findById(id)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        EventResponseDto response = eventService.updateEvent(id, request);

        assertThat(response.name()).isEqualTo("Jazz Concert");
        assertThat(response.capacity()).isEqualTo(200);
    }

    @Test
    void updateEvent_whenEventDoesNotExist_shouldThrowEventNotFoundException() {
        UUID id = UUID.randomUUID();

        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.updateEvent(id, buildRequest()))
                .isInstanceOf(EventNotFoundException.class);

        verify(eventRepository, never()).save(any());
    }

    @Test
    void deleteEvent_whenEventExists_shouldCallDeleteById() {
        UUID id = UUID.randomUUID();

        when(eventRepository.findById(id)).thenReturn(Optional.of(buildEvent(id)));

        eventService.deleteEvent(id);

        verify(eventRepository).deleteById(id);
    }

    @Test
    void deleteEvent_whenEventDoesNotExist_shouldThrowEventNotFoundExceptionWithoutDeleting() {
        UUID id = UUID.randomUUID();

        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.deleteEvent(id))
                .isInstanceOf(EventNotFoundException.class);

        verify(eventRepository, never()).deleteById(any());
    }
}
