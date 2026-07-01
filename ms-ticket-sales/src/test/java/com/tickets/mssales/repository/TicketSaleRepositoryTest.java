package com.tickets.mssales.repository;

import com.tickets.mssales.model.TicketSale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class TicketSaleRepositoryTest {

    @Autowired
    private TicketSaleRepository ticketSaleRepository;

    @Test
    void shouldSaveAndFindTicketSaleById() {
        TicketSale sale = TicketSale.builder()
                .eventId(UUID.randomUUID())
                .buyer("Juan Perez")
                .ticketCount(2)
                .total(new BigDecimal("50000"))
                .build();

        TicketSale saved = ticketSaleRepository.save(sale);

        assertTrue(ticketSaleRepository.findById(saved.getId()).isPresent());
    }
}
