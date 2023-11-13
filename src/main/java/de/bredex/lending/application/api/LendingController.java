package de.bredex.lending.application.api;

import de.bredex.lending.domain.model.Lending;
import de.bredex.lending.domain.service.LendingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public final class LendingController {

    private final LendingService service;

    public LendingController(final LendingService service) {
        this.service = service;
    }

    @PostMapping("/api/v1/lending")
    public ResponseEntity<LendingResponse> createLending(@RequestBody LendingRequest request) {
        final Lending lending = service.borrow(request.getAccountNumber(), request.getIsbn());
        return ResponseEntity.ok(
                new LendingResponse(lending.getAccountNumber(), lending.getIsbn(), lending.getReturnDate()));
    }

    @GetMapping("/api/v1/lending")
    public ResponseEntity<LendingsResponse> getLendings(@RequestParam String accountNumber) {
        final List<Lending> lendings = service.getLendings(accountNumber);

        return ResponseEntity.ok(new LendingsResponse(accountNumber,
                lendings.stream().map(lending -> new LendingsResponse.Item(lending.getIsbn(), lending.getReturnDate()))
                        .collect(Collectors.toList())));
    }

    @DeleteMapping("/api/v1/lending")
    public void removeLending(@RequestBody LendingRequest request) {
        service.deleteLending(request.getAccountNumber(), request.getIsbn());
    }
}
