package com.tickets.msevent.repository;

import com.tickets.msevent.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event buildEvent(String name, LocalDateTime date) {
        return Event.builder()
                .name(name)
                .description("Descripcion")
                .eventDate(date)
                .location("Estadio Nacional")
                .capacity(5000)
                .ticketPrice(new BigDecimal("25000.00"))
                .build();
    }

    @Test
    void shouldListEventsOrderedByDateAscending() {
        eventRepository.save(buildEvent("Evento Tarde", LocalDateTime.now().plusDays(2)));
        eventRepository.save(buildEvent("Evento Temprano", LocalDateTime.now().plusDays(1)));

        List<Event> result = eventRepository.findAllByOrderByEventDateAsc();

        assertEquals("Evento Temprano", result.get(0).getName());
    }

    @Test
    void shouldCheckIfEventExistsByNameAndDate() {
        LocalDateTime date = LocalDateTime.now().plusDays(3).withNano(0);
        eventRepository.save(buildEvent("Festival de Verano", date));

        assertTrue(eventRepository.existsByNameAndEventDate("Festival de Verano", date));
    }
}
