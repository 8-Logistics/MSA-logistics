package com.logistics.user.infrastructure.client;

import com.logistics.user.application.HubFeignService;
import com.logistics.user.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service",configuration = FeignConfig.class)
public interface HubFeignClient extends HubFeignService {

    @GetMapping(value = "/api/v1/hubs/{hubId}/exists")
    @Override
    boolean checkHub(@PathVariable("hubId") UUID hubId);

    @GetMapping(value = "/api/v1/hubs/user/{userId}")
    @Override
    UUID getUserHubId(@PathVariable("userId") Long userId);

}
