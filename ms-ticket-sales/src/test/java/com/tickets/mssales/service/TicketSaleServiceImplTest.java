package com.tickets.mssales.service;

import com.tickets.mssales.client.EventClient;
import com.tickets.mssales.dto.TicketSaleRequestDto;
import com.tickets.mssales.dto.TicketSaleResponseDto;
import com.tickets.mssales.exception.SaleNotFoundException;
import com.tickets.mssales.model.TicketSale;
import com.tickets.mssales.repository.TicketSaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketSaleServiceImplTest {

    @Mock
    private TicketSaleRepository ticketSaleRepository;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private TicketSaleServiceImpl ticketSaleService;

    private static final String AUTH_HEADER = "Bearer test-token";

    private TicketSale buildSale(UUID saleId, UUID eventId) {
        return TicketSale.builder()
                .id(saleId)
                .eventId(eventId)
                .buyer("Juan Perez")
                .ticketCount(2)
                .total(new BigDecimal("50000"))
                .saleDate(LocalDateTime.now())
                .build();
    }

    private TicketSaleRequestDto buildRequest(UUID eventId) {
        return new TicketSaleRequestDto(eventId, "Juan Perez", 2);
    }

    @Test
    void registrar_whenEventPriceIsRetrieved_shouldCalculateTotalAndPersist() {
        UUID eventId = UUID.randomUUID();
        UUID saleId = UUID.randomUUID();

        when(eventClient.getEventPrice(eventId, AUTH_HEADER)).thenReturn(new BigDecimal("25000"));
        when(ticketSaleRepository.save(any(TicketSale.class))).thenReturn(buildSale(saleId, eventId));

        TicketSaleResponseDto response = ticketSaleService.registrar(buildRequest(eventId), AUTH_HEADER);

        assertThat(response.id()).isEqualTo(saleId);
        assertThat(response.buyer()).isEqualTo("Juan Perez");
        assertThat(response.total()).isEqualByComparingTo("50000");
        verify(ticketSaleRepository).save(any(TicketSale.class));
    }

    @Test
    void registrar_shouldPassAuthHeaderToEventClient() {
        UUID eventId = UUID.randomUUID();

        when(eventClient.getEventPrice(eventId, AUTH_HEADER)).thenReturn(new BigDecimal("25000"));
        when(ticketSaleRepository.save(any(TicketSale.class))).thenReturn(buildSale(UUID.randomUUID(), eventId));

        ticketSaleService.registrar(buildRequest(eventId), AUTH_HEADER);

        verify(eventClient).getEventPrice(eventId, AUTH_HEADER);
    }

    @Test
    void buscarPorId_whenSaleExists_shouldReturnTicketSaleResponseDto() {
        UUID saleId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        when(ticketSaleRepository.findById(saleId)).thenReturn(Optional.of(buildSale(saleId, eventId)));

        TicketSaleResponseDto response = ticketSaleService.buscarPorId(saleId);

        assertThat(response.id()).isEqualTo(saleId);
        assertThat(response.eventId()).isEqualTo(eventId);
        assertThat(response.ticketCount()).isEqualTo(2);
    }

    @Test
    void buscarPorId_whenSaleDoesNotExist_shouldThrowSaleNotFoundException() {
        UUID saleId = UUID.randomUUID();

        when(ticketSaleRepository.findById(saleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketSaleService.buscarPorId(saleId))
                .isInstanceOf(SaleNotFoundException.class)
                .hasMessageContaining(saleId.toString());
    }

    @Test
    void listarTodas_shouldReturnMappedListOfResponseDtos() {
        UUID eventId = UUID.randomUUID();
        List<TicketSale> sales = List.of(
                buildSale(UUID.randomUUID(), eventId),
                buildSale(UUID.randomUUID(), eventId),
                buildSale(UUID.randomUUID(), eventId)
        );

        when(ticketSaleRepository.findAll()).thenReturn(sales);

        List<TicketSaleResponseDto> result = ticketSaleService.listarTodas();

        assertThat(result).hasSize(3);
        assertThat(result).allMatch(dto -> dto.buyer().equals("Juan Perez"));
    }

    @Test
    void listarTodas_whenNoSalesExist_shouldReturnEmptyList() {
        when(ticketSaleRepository.findAll()).thenReturn(List.of());

        assertThat(ticketSaleService.listarTodas()).isEmpty();
    }

    @Test
    void eliminar_whenSaleExists_shouldCallDeleteById() {
        UUID saleId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        when(ticketSaleRepository.findById(saleId)).thenReturn(Optional.of(buildSale(saleId, eventId)));

        ticketSaleService.eliminar(saleId);

        verify(ticketSaleRepository).deleteById(saleId);
    }

    @Test
    void eliminar_whenSaleDoesNotExist_shouldThrowSaleNotFoundExceptionWithoutDeleting() {
        UUID saleId = UUID.randomUUID();

        when(ticketSaleRepository.findById(saleId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketSaleService.eliminar(saleId))
                .isInstanceOf(SaleNotFoundException.class);

        verify(ticketSaleRepository, never()).deleteById(any());
    }
}
