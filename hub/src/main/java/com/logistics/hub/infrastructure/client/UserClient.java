package com.logistics.hub.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.logistics.hub.application.service.UserService;

@FeignClient(name = "user-service")
public interface UserClient extends UserService {
	// 허브 매니져로 롤 변경
	@PutMapping("/api/v1/users/{userId}/role")
	boolean updateUserRole(
		@PathVariable("userId") String userId,
		@RequestParam String role
	);

}
