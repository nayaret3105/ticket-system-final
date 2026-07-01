package com.tickets.mssales.service;

import com.tickets.mssales.client.EventClient;
import com.tickets.mssales.dto.MessageResponseDto;
import com.tickets.mssales.dto.TicketSaleRequestDto;
import com.tickets.mssales.dto.TicketSaleResponseDto;
import com.tickets.mssales.exception.SaleNotFoundException;
import com.tickets.mssales.model.TicketSale;
import com.tickets.mssales.repository.TicketSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketSaleServiceImpl implements TicketSaleService {

    private final TicketSaleRepository ticketSaleRepository;
    private final EventClient eventClient;

    private TicketSaleResponseDto toDto(TicketSale s) {
        return new TicketSaleResponseDto(s.getId(), s.getEventId(), s.getBuyer(),
                s.getTicketCount(), s.getTotal(), s.getSaleDate());
    }

    @Override
    public List<TicketSaleResponseDto> listarTodas() {
        return ticketSaleRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public TicketSaleResponseDto buscarPorId(UUID id) {
        return toDto(ticketSaleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Venta no encontrada: " + id)));
    }

    @Override
    public TicketSaleResponseDto registrar(TicketSaleRequestDto dto, String authHeader) {
        BigDecimal precio = eventClient.getEventPrice(dto.eventId(), authHeader);
        BigDecimal total = precio.multiply(BigDecimal.valueOf(dto.ticketCount()));

        TicketSale saved = ticketSaleRepository.save(TicketSale.builder()
                .eventId(dto.eventId())
                .buyer(dto.buyer())
                .ticketCount(dto.ticketCount())
                .total(total)
                .build());
        return toDto(saved);
    }

    @Override
    public MessageResponseDto eliminar(UUID id) {
        ticketSaleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Venta no encontrada: " + id));
        ticketSaleRepository.deleteById(id);
        return new MessageResponseDto("Venta eliminada: " + id);
    }
}
