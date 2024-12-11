package com.logistics.product.infrastucture.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubFeignClient {
    @GetMapping("/api/vi/hubs/{hubId}")
    public boolean checkHub(@PathVariable("hubId") UUID hubId);

    @GetMapping("/api/vi/hubs/{userId}")
    public UUID getUserHubId(String userId);
}
