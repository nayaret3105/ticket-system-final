package com.tickets.mssales.repository;

import com.tickets.mssales.model.TicketSale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TicketSaleRepository extends JpaRepository<TicketSale, UUID> {}
