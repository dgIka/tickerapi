package org.dgika;

import org.dgika.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class TickerApiApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TickerApiApp.class, args);
    }
}
