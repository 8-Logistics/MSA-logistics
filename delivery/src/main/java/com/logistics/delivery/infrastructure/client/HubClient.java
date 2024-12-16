package com.logistics.delivery.infrastructure.client;

import com.logistics.delivery.infrastructure.client.dto.HubPathResDto;
import com.logistics.delivery.infrastructure.client.dto.HubResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/api/v1/hubs/path")
    HubPathResDto getExactHubPath(
            @RequestParam("sourceHubId") UUID sourceHubId,
            @RequestParam("destinationHubId") UUID destinationHubId
    );

    @GetMapping("/api/v1/hubs/{hubId}")
    HubResDto getHub(@PathVariable UUID hubId);

}
