package org.dgika;

import org.dgika.model.User;
import org.dgika.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class TickerApiApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TickerApiApp.class, args);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test2@mail.ru")
                .passwordHash("testhash2")
                .isActive(true)
                .build();

//        UserService userService = run.getBean(UserService.class);
//        userService.addUser(user);
    }
}
