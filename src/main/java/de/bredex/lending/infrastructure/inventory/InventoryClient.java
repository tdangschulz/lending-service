package de.bredex.lending.infrastructure.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("inventory-service")
public interface InventoryClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/inventory/{isbn}")
    ResponseEntity<String> getBookByIsbn(@PathVariable String isbn);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/inventory" )
    ResponseEntity<List<BookDto>> getAllBooks();
}
