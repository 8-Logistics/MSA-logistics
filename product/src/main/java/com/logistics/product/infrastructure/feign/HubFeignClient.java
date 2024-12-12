package com.logistics.product.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubFeignClient {
    @GetMapping("/api/v1/hubs/{hubId}")
    public boolean checkHub(@PathVariable("hubId") UUID hubId);

    @GetMapping("/api/v1/hubs/{userId}")
    public UUID getUserHubId(String userId);
}
