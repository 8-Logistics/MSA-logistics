package com.logistics.user.application.service;

import com.logistics.user.application.dto.TokenDto;
import com.logistics.user.application.dto.UserSignInReqDto;
import com.logistics.user.application.dto.UserSignUpReqDto;
import org.springframework.http.ResponseEntity;


public interface AuthService {

    Boolean verifyUser(String username, String role);

    void signUp(UserSignUpReqDto request);

    TokenDto signIn(UserSignInReqDto request);

    TokenDto validateRefreshToken(String refreshToken);
}
