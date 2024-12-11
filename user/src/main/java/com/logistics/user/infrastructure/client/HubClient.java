package com.logistics.user.infrastructure.client;

import com.logistics.user.application.HubService;
import com.logistics.user.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service",configuration = FeignConfig.class)
public interface HubClient extends HubService {

    @GetMapping(value = "/api/v1/hubs/{hubId}")
    @Override
    Boolean getHubs(@PathVariable("hubId") UUID hubId);
}
