package org.dgika.api.controller;

import org.dgika.api.generated.dto.UserRegisterRequest;
import org.dgika.api.mapper.RegisterMapper;
import org.dgika.model.User;
import org.dgika.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/user/save")
public class TestController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserRegisterRequest req) {
        return userService.addUser(RegisterMapper.mapToUserCommand(req));
    }
}
