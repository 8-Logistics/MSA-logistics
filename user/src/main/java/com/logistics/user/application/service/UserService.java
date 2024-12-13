package com.logistics.user.application.service;

import com.logistics.user.application.dto.OrderUserDto;
import com.logistics.user.application.dto.UserModifyReqDto;
import com.logistics.user.application.dto.UserRoleUpdateDto;
import com.logistics.user.application.dto.UserSearchResDto;

public interface UserService {

    UserSearchResDto findUser(Long userId);

    void deleteUser(Long userId);

    UserSearchResDto modifyUser(Long userId, UserModifyReqDto request);

    boolean updateUserRole(Long userId, UserRoleUpdateDto request);

    OrderUserDto getUserInfo(Long userId);
}
