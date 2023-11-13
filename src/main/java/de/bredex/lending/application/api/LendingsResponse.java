package de.bredex.lending.application.api;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public final class LendingsResponse {

    private final String accountNumber;
    private final List<Item> items;

    public LendingsResponse(final String accountNumber, final List<Item> items) {
        this.accountNumber = accountNumber;
        this.items = items;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    static final class Item {

        private final String isbn;
        private final LocalDate returnDate;

        public Item(final String isbn, final LocalDate returnDate) {
            this.isbn = isbn;
            this.returnDate = returnDate;
        }

        public String getIsbn() {
            return isbn;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }
    }
}
