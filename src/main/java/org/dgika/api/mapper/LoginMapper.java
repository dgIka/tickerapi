package org.dgika.api.mapper;

import org.dgika.api.dto.LoginUserCommand;
import org.dgika.api.generated.dto.UserLoginRequest;
import org.springframework.stereotype.Component;


public class LoginMapper {

    public static LoginUserCommand mapToCommand(UserLoginRequest userLoginRequest) {
        return new LoginUserCommand(userLoginRequest.getEmail(), userLoginRequest.getPassword());
    }
}
