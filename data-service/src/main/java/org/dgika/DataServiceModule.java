package org.dgika;

import org.dgika.model.User;
import org.dgika.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DataServiceModule {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(DataServiceModule.class, args);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@mail.ru")
                .passwordHash("testhash")
                .build();

        UserService userService = ctx.getBean(UserService.class);

        userService.addUser(user);
    }
}
