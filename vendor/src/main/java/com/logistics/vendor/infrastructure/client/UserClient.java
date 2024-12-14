package com.logistics.vendor.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.logistics.vendor.application.dto.UserRoleUpdateDto;
import com.logistics.vendor.application.service.UserService;
import com.logistics.vendor.infrastructure.config.FeignConfig;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient extends UserService {
	// 허브 매니져로 롤 변경
	@PutMapping("/api/v1/users/{userId}/role")
	boolean updateUserRole(
		@PathVariable("userId") String userId,
		@RequestBody UserRoleUpdateDto request
	);

}
