package org.dgika.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dgika.api.generated.dto.UserLoginRequest;
import org.dgika.api.generated.dto.UserLoginResponse;
import org.dgika.api.generated.dto.UserRegisterRequest;
import org.dgika.api.generated.dto.UserSaveRequest;
import org.dgika.security.auth.UserDetailsImpl;
import org.dgika.security.service.AuthenticationService;
import org.dgika.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register (@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        userService.register(userRegisterRequest);
    }

    @PostMapping("/login")
    public UserLoginResponse login (@Valid @RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest);
        return new UserLoginResponse(token);
    }

    @PostMapping("/save")
    public void save (@Valid @RequestBody UserSaveRequest userSaveRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        System.out.println(userDetails.getUserId());
    }


}
