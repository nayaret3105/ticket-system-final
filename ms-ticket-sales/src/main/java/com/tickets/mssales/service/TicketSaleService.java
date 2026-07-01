package com.tickets.mssales.service;

import com.tickets.mssales.dto.MessageResponseDto;
import com.tickets.mssales.dto.TicketSaleRequestDto;
import com.tickets.mssales.dto.TicketSaleResponseDto;
import java.util.List;
import java.util.UUID;

public interface TicketSaleService {
    List<TicketSaleResponseDto> listarTodas();
    TicketSaleResponseDto buscarPorId(UUID id);
    TicketSaleResponseDto registrar(TicketSaleRequestDto dto, String authHeader);
    MessageResponseDto eliminar(UUID id);
}
