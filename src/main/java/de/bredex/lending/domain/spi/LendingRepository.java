package de.bredex.lending.domain.spi;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LendingRepository extends CrudRepository<LendingEntity, Integer> {

    List<LendingEntity> findAllByAccountNumber(String accountNumber);

    Optional<LendingEntity> findByAccountNumberAndIsbn(String accountNumber, String isbn);
}
