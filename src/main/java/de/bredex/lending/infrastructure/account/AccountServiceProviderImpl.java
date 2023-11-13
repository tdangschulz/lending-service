package de.bredex.lending.infrastructure.account;

import de.bredex.lending.domain.spi.AccountServiceProvider;
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
public class AccountServiceProviderImpl implements AccountServiceProvider {

    private final AccountClient accountClient;
    private final Set<String> accountNumbers = new HashSet<>();

    public AccountServiceProviderImpl(final AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @Override
    public boolean accountExists(String accountNumber) {
        try {
            final ResponseEntity<String> response = accountClient.getAccountByAccountNumber(accountNumber);

            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            // Do nothing.
        }

        return accountNumbers.contains(accountNumber);
    }

    @Scheduled(fixedRate = 5000)
    public void updateAccounts() {
        try {
            final ResponseEntity<List<AccountDto>> response = accountClient.getAllAccounts();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                accountNumbers.clear();
                for (AccountDto account : response.getBody()) {
                    accountNumbers.add(account.getNumber());
                }
            }
        } catch (ResourceAccessException e) {
            // Do nothing.
        }
    }
}
