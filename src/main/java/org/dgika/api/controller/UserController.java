package org.dgika.api.controller;

import lombok.RequiredArgsConstructor;
import org.dgika.api.generated.dto.UserLoginRequest;
import org.dgika.api.generated.dto.UserLoginResponse;
import org.dgika.api.generated.dto.UserRegisterRequest;
import org.dgika.security.service.AuthentificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthentificationService authentificationService;

    @PostMapping("/register")
    public UserLoginResponse register (@RequestBody UserRegisterRequest userRegisterRequest) {
        String token = authentificationService.register(userRegisterRequest);
        return new UserLoginResponse(token);
    }

    @PostMapping("/login")
    public UserLoginResponse login (@RequestBody UserLoginRequest userLoginRequest) {
        String token = authentificationService.login(userLoginRequest);
        return new UserLoginResponse(token);
    }

    @GetMapping("/secure")
    public String secure() {
        return "JWT OK";
    }

}
