package com.logistics.user.application.service;

import com.logistics.user.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {

    UserSearchResDto findUser(Long userId);

    void deleteUser(Long userId);

    UserSearchResDto modifyUser(Long userId, UserModifyReqDto request);

    boolean updateUserRole(Long userId, UserRoleUpdateDto request);

    OrderUserDto getUserInfo(String userId);

    Page<UserSearchResDto> searchUsers(UserSearchReqDto searchRequest,
                                                       Pageable pageable, String userId, String role);
}
