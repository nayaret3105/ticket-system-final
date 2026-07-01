package com.tickets.mssales.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket_sales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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
    protected void onCreate() { saleDate = LocalDateTime.now(); }
}
