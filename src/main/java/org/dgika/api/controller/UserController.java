package org.dgika.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dgika.api.generated.dto.*;
import org.dgika.security.auth.UserDetailsImpl;
import org.dgika.service.PriceService;
import org.dgika.service.TickerService;
import org.dgika.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TickerService tickerService;
    private final PriceService priceService;

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
    @ResponseStatus(HttpStatus.CREATED)
    public List<TickerDay> save (@Valid @RequestBody UserSaveRequest userSaveRequest, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return tickerService.findAndSave(userSaveRequest, userDetails.getUserId());
    }

    @GetMapping("/saved")
    public UserSavedResponse saved(@RequestParam("ticker") String tickerName, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return priceService.findAllByUserIdAndTickerName(userDetails.getUserId(), tickerName);
    }


}
