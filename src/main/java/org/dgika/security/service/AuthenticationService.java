package org.dgika.security.service;

import lombok.RequiredArgsConstructor;
import org.dgika.api.dto.RegisterUserCommand;
import org.dgika.api.exception.BadRequestException;
import org.dgika.api.generated.dto.UserLoginRequest;
import org.dgika.api.generated.dto.UserRegisterRequest;
import org.dgika.model.User;
import org.dgika.repository.UserRepository;
import org.dgika.security.auth.UserDetailsImpl;
import org.dgika.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterUserCommand request) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .build();

        userRepository.save(user);

        UserDetailsImpl principal = new UserDetailsImpl(user);
        return jwtService.generateToken(principal);
    }


    public String login(UserLoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid email or password");
        }

        UserDetailsImpl principal =
                (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getEmail());

        return jwtService.generateToken(principal);
    }
}
