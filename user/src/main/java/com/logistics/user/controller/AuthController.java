package com.logistics.user.controller;

import com.logistics.user.application.dto.ApiResponse;
import com.logistics.user.application.dto.TokenDto;
import com.logistics.user.application.dto.UserSignInReqDto;
import com.logistics.user.application.dto.UserSignUpReqDto;
import com.logistics.user.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth-Service API", description = "사용자 인증/검증 API Controller")
@RequiredArgsConstructor
public class AuthController {

    private final HttpServletRequest servletRequest;
    private final AuthService authService;

    @Operation(summary = "[Feign] Jwt AccessToken 내부 값 검증 API", description = "gateway -> user-service 토큰 내부값 있는지 검증")
    @GetMapping("/auth/verify")
    public ResponseEntity<Boolean> verifyUser(@RequestParam(value = "userId") String userId,
                                              @RequestParam(value = "role") String role) {
        Boolean response = authService.verifyUser(userId, role);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원가입 API", description = "모든 파라미터 필수")
    @Parameter(name = "username", description = "로그인 ID, 4자 이상 10자 이하의 소문자 알파벳과 숫자로만 구성")
    @Parameter(name = "password", description = "비밀번호는 8자 이상 15자 이하이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함")
    @PostMapping("/auth/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpReqDto request) {

        servletRequest.setAttribute("username", request.getUsername());
        authService.signUp(request);

        return ResponseEntity.ok(ApiResponse.success("회원가입 Success"));
    }

    @Operation(summary = "로그인 API", description = "로그인 성공 후 accesstoken, refreshtoken header로 응답")
    @PostMapping("/auth/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserSignInReqDto request) {

        TokenDto tokenDto = authService.signIn(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenDto.getAccessToken());
        headers.set("Refresh-Authorization", tokenDto.getRefreshToken());

        return new ResponseEntity<>(ApiResponse.success("login Success"), headers, HttpStatus.OK);
    }

    @Operation(summary = "refreshToken 검증 후 AccessToken 재발급 API",
            description = "redis에 있는 refreshToken과 header refreshToken 검증 후 accessToken 재발급")
    @GetMapping("/auth/refresh")
    public ResponseEntity<?> validateRefreshToken(@RequestHeader("Refresh-Authorization") String refreshToken) {

        TokenDto tokenDto = authService.validateRefreshToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenDto.getAccessToken());

        return new ResponseEntity<>(ApiResponse.success("AccessToken 재발급 성공"), headers, HttpStatus.OK);
    }

}
