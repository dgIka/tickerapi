package org.dgika.service;

import org.dgika.api.dto.RegisterUserCommand;
import org.dgika.model.User;
import org.dgika.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(RegisterUserCommand registerUserCommand) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(registerUserCommand.getName())
                .email(registerUserCommand.getEmail())
                .passwordHash(registerUserCommand.getPassword())
                .build();
        return userRepository.save(user);
    }
}
