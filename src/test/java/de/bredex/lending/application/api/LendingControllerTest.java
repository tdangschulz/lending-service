package de.bredex.lending.application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bredex.lending.infrastructure.account.AccountClient;
import de.bredex.lending.infrastructure.inventory.InventoryClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class LendingControllerTest {

    @MockBean
    private AccountClient accountClient;
    @MockBean
    private InventoryClient inventoryClient;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void simulateAccountService() {
        Mockito.when(accountClient.getAccountByAccountNumber(Mockito.anyString()))
                .thenReturn(ResponseEntity.ok("{\"number\":\"10001\"}"));
        Mockito.when(inventoryClient.getBookByIsbn(Mockito.anyString()))
                .thenReturn(ResponseEntity.ok("{\"isbn\":\"1-86092-038-1\"}"));
    }

    @Test
    void POST_createLending_creates_new_lending() throws Exception {
        createLending("10001", "1-86092-038-1")
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.accountNumber", is("10001")))
                .andExpect(jsonPath("$.isbn", is("1-86092-038-1")));

        deleteLending("10001", "1-86092-038-1");
    }

    @Test
    void GET_returns_all_lendings_of_account() throws Exception {
        createLending("10001", "1-86092-038-1");
        createLending("10001", "1-86092-029-5");

        mvc.perform(get("/api/v1/lending").queryParam("accountNumber", "10001"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.accountNumber", is("10001")))
                .andExpect(jsonPath("$.items", hasSize(2)));

        deleteLending("10001", "1-86092-038-1");
        deleteLending("10001", "1-86092-029-5");
    }

    @Test
    void DELETE_deletes_existing_lending() throws Exception {
        createLending("10001", "1-86092-038-1");

        deleteLending("10001", "1-86092-038-1")
                .andExpect(status().is(HttpStatus.OK.value()));

        mvc.perform(get("/api/v1/lending").queryParam("accountNumber", "10001"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.accountNumber", is("10001")))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void DELETE_returns_400_on_non_existing_lending() throws Exception {
        deleteLending("10001", "1-86092-038-1")
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    private ResultActions createLending(final String accountNumber, final String isbn) throws Exception {
        final LendingRequest request = new LendingRequest(accountNumber, isbn);
        byte[] input = mapper.writeValueAsBytes(request);

        return mvc.perform(post("/api/v1/lending")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(input));
    }

    private ResultActions deleteLending(final String accountNumber, final String isbn) throws Exception {
        final LendingRequest request = new LendingRequest(accountNumber, isbn);
        byte[] input = mapper.writeValueAsBytes(request);

        return mvc.perform(delete("/api/v1/lending")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(input));
    }
}
