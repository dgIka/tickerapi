package org.dgika.service;

import org.dgika.api.dto.RegisterUserCommand;
import org.dgika.model.Price;
import org.dgika.model.User;
import org.dgika.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User addUser(RegisterUserCommand registerUserCommand) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(registerUserCommand.getName())
                .email(registerUserCommand.getEmail())
                .passwordHash(registerUserCommand.getPassword())
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public User saveUserPrices(UUID userId, List<Price> prices) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            prices.forEach(price -> {user.getPrices().add(price);});
        }
        return user;
    }


}
