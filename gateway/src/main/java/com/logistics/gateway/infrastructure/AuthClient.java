package com.logistics.gateway.infrastructure;

import com.logistics.gateway.application.AuthService;
import com.logistics.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface AuthClient extends AuthService {

    @GetMapping("/api/v1/auth/verify")
    Boolean verifyUser(@RequestParam(value = "userId") String userId, @RequestParam(value = "role") String role);

}
