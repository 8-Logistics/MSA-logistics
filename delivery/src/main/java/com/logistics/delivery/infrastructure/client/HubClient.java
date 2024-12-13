package com.logistics.delivery.infrastructure.client;

import com.logistics.delivery.infrastructure.client.dto.HubPathResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {

    @GetMapping("/api/v1/hubs/{sourceHubId}/paths")
    HubPathResDto getHubPath(
            @PathVariable("sourceHubId") UUID sourceHubId,
            @RequestParam("destination_hub_id") UUID destinationHubId
    );
}
