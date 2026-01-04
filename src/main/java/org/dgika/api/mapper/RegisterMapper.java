package org.dgika.api.mapper;

import org.dgika.api.dto.RegisterUserCommand;
import org.dgika.api.generated.dto.UserRegisterRequest;
import org.springframework.stereotype.Component;


public class RegisterMapper {

    public static RegisterUserCommand mapToUserCommand(UserRegisterRequest req) {
        return new RegisterUserCommand(req.getName(), req.getEmail(), req.getPassword());
    }

}
