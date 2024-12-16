package com.logistics.hub.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.logistics.hub.application.dto.UserRoleUpdateDto;
import com.logistics.hub.application.service.UserService;
import com.logistics.hub.infrastructure.config.FeignConfig;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient extends UserService {

	@PutMapping("/api/v1/users/{userId}/role")
	boolean updateUserRole(
		@PathVariable("userId") long userId,
		@RequestBody UserRoleUpdateDto request
	);

}
