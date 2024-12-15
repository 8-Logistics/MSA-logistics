package com.logistics.vendor.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.logistics.vendor.application.service.HubService;
import com.logistics.vendor.infrastructure.config.FeignConfig;

@FeignClient(name = "hub-service", configuration = FeignConfig.class)
public interface HubClient extends HubService {
	@GetMapping("api/v1/hubs/{hubId}/exists")
	public boolean checkHub(@PathVariable("hubId") UUID hubId);

}
