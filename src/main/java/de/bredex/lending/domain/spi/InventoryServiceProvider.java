package de.bredex.lending.domain.spi;

public interface InventoryServiceProvider {

    boolean bookExists(final String isbn);
}
