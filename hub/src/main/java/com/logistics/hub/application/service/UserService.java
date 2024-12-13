package com.logistics.hub.application.service;

import com.logistics.hub.application.dto.UserRoleUpdateDto;

// 응용 계층 DIP 적용을 위한 유저 서비스 인터페이스
public interface UserService {
	boolean updateUserRole(String userId, UserRoleUpdateDto request);
}
