package de.bredex.lending.domain.service;

import de.bredex.lending.domain.model.Lending;
import de.bredex.lending.domain.spi.AccountServiceProvider;
import de.bredex.lending.domain.spi.InventoryServiceProvider;
import de.bredex.lending.domain.spi.LendingEntity;
import de.bredex.lending.domain.spi.LendingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LendingServiceTest {

    private final AccountServiceProvider accountService = mock(AccountServiceProvider.class);
    private final InventoryServiceProvider inventoryService = mock(InventoryServiceProvider.class);
    private final LendingRepository repository = mock(LendingRepository.class);

    private LendingService service;

    @BeforeEach
    public void setUp() {
        service = new LendingService(accountService, inventoryService, repository);
    }

    @Test
    void borrow_creates_new_lending() {
        when(accountService.accountExists(any())).thenReturn(true);
        when(inventoryService.bookExists(any())).thenReturn(true);
        when(repository.save(any()))
                .thenReturn(new LendingEntity("10001", "1-86092-038-1", LocalDate.now().plusWeeks(4)));

        Lending lending = service.borrow("10001", "1-86092-038-1");

        assertThat(lending.getAccountNumber()).isEqualTo("10001");
        assertThat(lending.getIsbn()).isEqualTo("1-86092-038-1");
        assertThat(lending.getReturnDate()).isEqualTo(LocalDate.now().plusWeeks(4));
    }

    @Test
    void borrow_throws_exception_for_lending_request_with_non_existing_account() {
        when(accountService.accountExists(any())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.borrow("10001", "1-86092-038-1"));
    }

    @Test
    void borrow_throws_exception_for_lending_request_with_non_existing_book() {
        when(accountService.accountExists(any())).thenReturn(true);
        when(inventoryService.bookExists(any())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.borrow("10001", "1-86092-038-1"));
    }

    @Test
    void getLendings_returns_lendings() {
        final List<LendingEntity> storedLendings = new LinkedList<>();
        storedLendings.add(new LendingEntity("10001", "1-86092-038-1", LocalDate.now().plusWeeks(4)));
        storedLendings.add(new LendingEntity("10001", "1-86092-025-9", LocalDate.now().plusWeeks(4)));
        when(repository.findAllByAccountNumber(any())).thenReturn(storedLendings);

        final List<Lending> lendings = service.getLendings("10001");

        assertThat(lendings).hasSize(2);
    }

    @Test
    void deleteLending_throws_exception_for_non_existing_lending() {
        when(repository.findByAccountNumberAndIsbn(any(), any())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteLending("10001", "1-86092-038-1"));
    }

    @Test
    void deleteLending_deletes_existing_lending() {
        final LendingEntity lendingEntity = new LendingEntity("10001", "1-86092-038-1",
                LocalDate.now().plusWeeks(4));
        when(repository.findByAccountNumberAndIsbn(any(), any())).thenReturn(Optional.of(lendingEntity));

        service.deleteLending("10001", "1-86092-038-1");

        verify(repository, times(1)).delete(lendingEntity);
    }
}
