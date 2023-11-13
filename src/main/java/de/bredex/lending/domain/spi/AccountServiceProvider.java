package de.bredex.lending.domain.spi;

public interface AccountServiceProvider {

    boolean accountExists(final String accountNumber);
}
