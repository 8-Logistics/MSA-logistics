package com.logistics.vendor.application.service;

import com.logistics.vendor.application.dto.UserRoleUpdateDto;

// 응용 계층 DIP 적용을 위한 유저 서비스 인터페이스
public interface UserService {
	boolean updateUserRole(long userId, UserRoleUpdateDto request);
}
