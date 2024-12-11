package com.logistics.user.application.service;

import com.logistics.user.application.dto.UserSearchResDto;

public interface UserService {

    UserSearchResDto findUser(Long userId);

}
