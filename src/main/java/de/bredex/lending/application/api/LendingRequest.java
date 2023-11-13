package de.bredex.lending.application.api;

public final class LendingRequest {

    private final String accountNumber;
    private final String isbn;

    public LendingRequest(final String accountNumber, final String isbn) {
        this.accountNumber = accountNumber;
        this.isbn = isbn;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getIsbn() {
        return isbn;
    }
}
