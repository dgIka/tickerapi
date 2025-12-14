package org.dgika.controller;

import org.dgika.api.ApiApi;
import org.dgika.dto.UserLoginRequest;
import org.dgika.dto.UserRegisterRequest;
import org.dgika.dto.UserSaveRequest;
import org.dgika.dto.UserSavedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

public class UserDataController implements ApiApi {

    @Override
    public ResponseEntity<Void> apiUserLoginPost(UserLoginRequest userLoginRequest) {
        return ApiApi.super.apiUserLoginPost(userLoginRequest);
    }

    @Override
    public ResponseEntity<Void> apiUserRegisterPost(UserRegisterRequest userRegisterRequest) {
        return ApiApi.super.apiUserRegisterPost(userRegisterRequest);
    }

    @Override
    public ResponseEntity<Void> apiUserSavePost(UserSaveRequest userSaveRequest) {
        return ApiApi.super.apiUserSavePost(userSaveRequest);
    }

    @Override
    public ResponseEntity<UserSavedResponse> apiUserSavedGet(String ticker) {
        return ApiApi.super.apiUserSavedGet(ticker);
    }
}
