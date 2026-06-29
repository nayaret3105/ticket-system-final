package com.tickets.mssales.service;

import com.tickets.mssales.dto.request.TicketSaleRequestDto;
import com.tickets.mssales.dto.response.TicketSaleResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Contract for ticket sale operations.
 */
public interface TicketSaleService {

    /**
     * Registers a new ticket sale. Fetches the event price from ms-event to calculate the total.
     *
     * @param request    the sale data (event id, buyer, ticket count)
     * @param authHeader the Authorization header forwarded to ms-event
     * @return the persisted sale
     * @throws com.tickets.mssales.exception.BusinessException if the event does not exist or ms-event is unreachable
     */
    TicketSaleResponseDto createSale(TicketSaleRequestDto request, String authHeader);

    /**
     * Retrieves a sale by its identifier.
     *
     * @param id the sale UUID
     * @return the sale data
     * @throws com.tickets.mssales.exception.SaleNotFoundException if no sale with the given id exists
     */
    TicketSaleResponseDto getSaleById(UUID id);

    /**
     * Returns all registered sales.
     *
     * @return list of all sales
     */
    List<TicketSaleResponseDto> getAllSales();

    /**
     * Permanently removes a sale record.
     *
     * @param id the sale to delete
     * @throws com.tickets.mssales.exception.SaleNotFoundException if no sale with the given id exists
     */
    void deleteSale(UUID id);
}
