package com.tickets.msevent.repository;

import com.tickets.msevent.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findAllByOrderByEventDateAsc();
    boolean existsByNameAndEventDate(String name, LocalDateTime eventDate);
}
