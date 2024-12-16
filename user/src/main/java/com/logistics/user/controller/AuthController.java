package com.logistics.user.controller;

import com.logistics.user.application.dto.ApiResponse;
import com.logistics.user.application.dto.TokenDto;
import com.logistics.user.application.dto.UserSignInReqDto;
import com.logistics.user.application.dto.UserSignUpReqDto;
import com.logistics.user.application.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final HttpServletRequest servletRequest;
    private final AuthService authService;

    @GetMapping("/auth/verify")
    public ResponseEntity<Boolean> verifyUser(@RequestParam(value = "userId") String userId,
                                              @RequestParam(value = "role") String role) {
        Boolean response = authService.verifyUser(userId, role);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpReqDto request) {

        servletRequest.setAttribute("username", request.getUsername());
        authService.signUp(request);

        return ResponseEntity.ok(ApiResponse.success("회원가입 Success"));
    }

    @PostMapping("/auth/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserSignInReqDto request) {

        TokenDto tokenDto = authService.signIn(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenDto.getAccessToken());
        headers.set("Refresh-Authorization", tokenDto.getRefreshToken());

        return new ResponseEntity<>(ApiResponse.success("login Success"), headers, HttpStatus.OK);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<?> validateRefreshToken(@RequestHeader("Refresh-Authorization") String refreshToken) {

        TokenDto tokenDto = authService.validateRefreshToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenDto.getAccessToken());

        return new ResponseEntity<>(ApiResponse.success("AccessToken 재발급 성공"), headers, HttpStatus.OK);
    }

}
