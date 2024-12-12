package com.logistics.user.controller;

import com.logistics.user.application.dto.UserSignInReqDto;
import com.logistics.user.application.dto.UserSignUpReqDto;
import com.logistics.user.application.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpReqDto request){

        servletRequest.setAttribute("username", request.getUsername());
        authService.signUp(request);

        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/auth/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserSignInReqDto request){
        return authService.signIn(request);
    }



}
