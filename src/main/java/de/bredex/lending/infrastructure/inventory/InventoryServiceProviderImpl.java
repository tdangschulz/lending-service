package de.bredex.lending.infrastructure.inventory;

import de.bredex.lending.domain.spi.InventoryServiceProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InventoryServiceProviderImpl implements InventoryServiceProvider {

    private final InventoryClient inventoryClient;
    private final Set<String> isbns = new HashSet<>();

    public InventoryServiceProviderImpl(final InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @Override
    public boolean bookExists(String isbn) {
        try {
            final ResponseEntity<String> response = inventoryClient.getBookByIsbn(isbn);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (final RestClientException exception) {
            // Do nothing.
        }

        return isbns.contains(isbn);
    }

    @Scheduled(fixedRate = 5000)
    public void updateInventory() {
        try {
            final ResponseEntity<List<BookDto>> response = inventoryClient.getAllBooks();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                isbns.clear();
                for (BookDto book : response.getBody()) {
                    isbns.add(book.getIsbn());
                }
            }
        } catch (ResourceAccessException e) {
            // Do nothing.
        }
    }
}
