package com.logistics.order.infrastructure.client;

import com.logistics.order.application.config.FeignConfig;
import com.logistics.order.application.dto.OrderUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserFeignClient {
    @GetMapping("/api/v1/users/userInfo/{userId}")
    OrderUserDto getUserInfo(@PathVariable("userId") String userId);

}
