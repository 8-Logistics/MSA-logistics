package com.logistics.user.controller;

import com.logistics.user.application.CustomPrincipal;
import com.logistics.user.application.dto.*;
import com.logistics.user.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 단건 조회 API
     *
     * @param userId
     * @return
     */
    @Operation(summary = "사용자 단건 조회 API")
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> findUser(@PathVariable("userId") Long userId) {

        UserSearchResDto response = userService.findUser(userId);
        return ResponseEntity.ok(ApiResponse.success("user 단건 조회 success", response));
    }

    /**
     * 사용자 삭제 API
     *
     * @param userId
     * @return
     */
    @Operation(summary = "사용자 삭제 API", description = "MASTER 권한만 가능")
    @PreAuthorize("hasAnyAuthority('MASTER')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {

        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("user delete success"));
    }

    /**
     *  사용자 수정 API
     *
     * @param userId
     * @param request
     * @return
     */
    @Operation(summary = "사용자 수정 API", description = "MASTER 권한만 가능")
    @PreAuthorize("hasAnyAuthority('MASTER')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> modifyUser(@PathVariable("userId") Long userId, @RequestBody UserModifyReqDto request) {

        UserSearchResDto response = userService.modifyUser(userId, request);

        return ResponseEntity.ok(ApiResponse.success("user 수정 success", response));
    }

    // [Feign] 허브 담당자 & 업체 담당자 role 수정 API
    @PreAuthorize("hasAnyAuthority('MASTER')")
    @PutMapping("/users/{userId}/role")
    public boolean updateUserRole(@PathVariable("userId") Long userId, @RequestBody UserRoleUpdateDto request){
        return userService.updateUserRole(userId, request);
    }

    // [Feign] order 주문에 slackId & 이름 return API
    @GetMapping("/users/userInfo/{userName}")
    public OrderUserDto getUserInfo(@PathVariable("userName") String userName) {
        return userService.getUserInfo(userName);
    }

    // user 검색, 조회 API
    @Operation(summary = "사용자 검색/조회 API")
    @PreAuthorize("hasAnyAuthority('MASTER','DELIVERY_MANAGER', 'HUB_MANAGER')")
    @GetMapping("/users/userSearch")
    public ResponseEntity<?> searchUsers(UserSearchReqDto searchRequest,
                                                             Pageable pageable,
                                                             @AuthenticationPrincipal CustomPrincipal principal){

        Page<UserSearchResDto> response = userService.searchUsers(searchRequest, pageable, principal.getUserId(), principal.getRole());

        return ResponseEntity.ok(ApiResponse.success("user 조회/검색 success", response));
    }


}
