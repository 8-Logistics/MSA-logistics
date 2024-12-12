package com.logistics.user.controller;

import com.logistics.user.application.CustomPrincipal;
import com.logistics.user.application.dto.UserSearchResDto;
import com.logistics.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('NORMAL')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserSearchResDto> findUser(@AuthenticationPrincipal CustomPrincipal customPrincipal,
            @PathVariable("userId") Long userId) {

        UserSearchResDto response = userService.findUser(userId);
        return ResponseEntity.ok(response);
    }



}
