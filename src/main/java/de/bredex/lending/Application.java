package de.bredex.lending;

import de.bredex.lending.infrastructure.account.AccountClient;
import de.bredex.lending.infrastructure.inventory.InventoryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(clients = {InventoryClient.class, AccountClient.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
