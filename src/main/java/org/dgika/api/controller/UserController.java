package org.dgika.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dgika.api.generated.dto.UserLoginRequest;
import org.dgika.api.generated.dto.UserLoginResponse;
import org.dgika.api.generated.dto.UserRegisterRequest;
import org.dgika.security.service.AuthenticationService;
import org.dgika.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserLoginResponse register (@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        String token = userService.register(userRegisterRequest);
        return new UserLoginResponse(token);
    }

    @PostMapping("/login")
    public UserLoginResponse login (@Valid @RequestBody UserLoginRequest userLoginRequest) {
        String token = authenticationService.login(userLoginRequest);
        return new UserLoginResponse(token);
    }


}
