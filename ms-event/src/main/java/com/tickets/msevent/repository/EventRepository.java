package com.tickets.msevent.repository;

import com.tickets.msevent.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for {@link Event} entities.
 */
public interface EventRepository extends JpaRepository<Event, UUID> {

    /** Returns {@code true} if an event with the same name and date already exists. */
    boolean existsByNameAndEventDate(String name, LocalDateTime eventDate);

    /** Returns all events ordered by their date ascending. */
    List<Event> findAllByOrderByEventDateAsc();
}
