package com.tickets.mssales.service.impl;

import com.tickets.mssales.client.EventClient;
import com.tickets.mssales.dto.request.TicketSaleRequestDto;
import com.tickets.mssales.dto.response.TicketSaleResponseDto;
import com.tickets.mssales.entity.TicketSale;
import com.tickets.mssales.exception.SaleNotFoundException;
import com.tickets.mssales.repository.TicketSaleRepository;
import com.tickets.mssales.service.TicketSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Default implementation of {@link TicketSaleService}.
 */
@Service
@RequiredArgsConstructor
public class TicketSaleServiceImpl implements TicketSaleService {

    private final TicketSaleRepository ticketSaleRepository;
    private final EventClient eventClient;

    /**
     * {@inheritDoc}
     * Calls ms-event to retrieve the ticket price, then computes total = price × ticketCount.
     */
    @Override
    public TicketSaleResponseDto createSale(TicketSaleRequestDto request, String authHeader) {
        BigDecimal price = eventClient.getEventPrice(request.eventId(), authHeader);
        BigDecimal total = price.multiply(BigDecimal.valueOf(request.ticketCount()));

        TicketSale sale = TicketSale.builder()
                .eventId(request.eventId())
                .buyer(request.buyer())
                .ticketCount(request.ticketCount())
                .total(total)
                .build();

        return toResponse(ticketSaleRepository.save(sale));
    }

    /** {@inheritDoc} */
    @Override
    public TicketSaleResponseDto getSaleById(UUID id) {
        return toResponse(findById(id));
    }

    /** {@inheritDoc} */
    @Override
    public List<TicketSaleResponseDto> getAllSales() {
        return ticketSaleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public void deleteSale(UUID id) {
        findById(id);
        ticketSaleRepository.deleteById(id);
    }

    private TicketSale findById(UUID id) {
        return ticketSaleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Venta no encontrada con id: " + id));
    }

    private TicketSaleResponseDto toResponse(TicketSale sale) {
        return new TicketSaleResponseDto(
                sale.getId(),
                sale.getEventId(),
                sale.getBuyer(),
                sale.getTicketCount(),
                sale.getTotal(),
                sale.getSaleDate()
        );
    }
}
