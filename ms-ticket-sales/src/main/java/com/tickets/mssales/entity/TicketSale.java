package com.tickets.mssales.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity representing the purchase of one or more tickets for an event.
 */
@Entity
@Table(name = "ticket_sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketSale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(nullable = false, length = 150)
    private String buyer;

    @Column(name = "ticket_count", nullable = false)
    private Integer ticketCount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "sale_date", nullable = false, updatable = false)
    private LocalDateTime saleDate;

    @PrePersist
    protected void onCreate() {
        saleDate = LocalDateTime.now();
    }
}
