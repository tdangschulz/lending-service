package de.bredex.lending.infrastructure.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("account-service")
public interface AccountClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/account/{accountNumber}")
    ResponseEntity<String> getAccountByAccountNumber(@PathVariable String accountNumber);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/account" )
    ResponseEntity<List<AccountDto>> getAllAccounts();
}
