package com.logistics.user.controller;

import com.logistics.user.application.dto.OrderUserDto;
import com.logistics.user.application.dto.UserModifyReqDto;
import com.logistics.user.application.dto.UserRoleUpdateDto;
import com.logistics.user.application.dto.UserSearchResDto;
import com.logistics.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserSearchResDto> findUser(@PathVariable("userId") Long userId) {

        UserSearchResDto response = userService.findUser(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 삭제 API
     *
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MASTER')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {

        userService.deleteUser(userId);
        return ResponseEntity.ok("사용자 삭제 성공");
    }

    /**
     * TODO 공통 응답으로 다 바꾸기!!
     *  사용자 수정 API
     *
     * @param userId
     * @param request
     * @return
     */
    @PreAuthorize("hasAnyAuthority('MASTER')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> modifyUser(@PathVariable("userId") Long userId, @RequestBody UserModifyReqDto request) {

        UserSearchResDto response = userService.modifyUser(userId, request);

        return ResponseEntity.ok("수정이 완료 되었습니다.");
    }

    // [Feign] 허브 담당자 & 업체 담당자 role 수정 API
    @PreAuthorize("hasAnyAuthority('MASTER')")
    @PutMapping("/users/{userId}/role")
    public boolean updateUserRole(@PathVariable("userId") Long userId, @RequestBody UserRoleUpdateDto request){
        return userService.updateUserRole(userId, request);
    }

    // [Feign] order 주문에 slackId & 이름 return API
    @GetMapping("/users/userInfo/{userId}")
    public OrderUserDto getUserInfo(@PathVariable("userId") Long userId) {
        return userService.getUserInfo(userId);
    }


}
