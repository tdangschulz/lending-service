package de.bredex.lending.domain.spi;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "lendings")
public final class LendingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String accountNumber;
    private String isbn;
    private LocalDate returnDate;

    public LendingEntity() {
    }

    public LendingEntity(final String accountNumber, final String isbn, final LocalDate returnDate) {
        this.accountNumber = accountNumber;
        this.isbn = isbn;
        this.returnDate = returnDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
