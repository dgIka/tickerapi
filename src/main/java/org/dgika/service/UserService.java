package org.dgika.service;

import lombok.RequiredArgsConstructor;
import org.dgika.api.dto.LoginUserCommand;
import org.dgika.api.dto.RegisterUserCommand;
import org.dgika.api.exception.BadRequestException;
import org.dgika.api.generated.dto.UserLoginRequest;
import org.dgika.api.generated.dto.UserRegisterRequest;
import org.dgika.model.Price;
import org.dgika.model.User;
import org.dgika.repository.UserRepository;
import org.dgika.security.service.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;


    @Transactional
    public String register(UserRegisterRequest urr) {

        RegisterUserCommand ruc = RegisterUserCommand.builder()
                .email(urr.getEmail())
                .name(urr.getName())
                .password(urr.getPassword())
                .build();

        if (userRepository.existsByEmail(ruc.getEmail())) {
            throw new BadRequestException("User already exists");
        }

        return authenticationService.register(ruc);
    }

    public String login(UserLoginRequest urr) {
        LoginUserCommand luc = LoginUserCommand.builder()
                .email(urr.getEmail())
                .password(urr.getPassword())
                .build();

        return authenticationService.login(luc);
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
