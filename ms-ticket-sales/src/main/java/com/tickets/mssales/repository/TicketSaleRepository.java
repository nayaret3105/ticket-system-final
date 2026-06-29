package com.tickets.mssales.repository;

import com.tickets.mssales.entity.TicketSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for {@link TicketSale} entities.
 */
public interface TicketSaleRepository extends JpaRepository<TicketSale, UUID> {

    /** Returns all sales for a given event. */
    List<TicketSale> findByEventId(UUID eventId);

    /** Returns all sales whose buyer name contains the given string (case-insensitive). */
    List<TicketSale> findByBuyerContainingIgnoreCase(String buyer);
}
