package com.logistics.delivery.infrastructure.client;

import com.logistics.delivery.infrastructure.client.dto.DeliveryManagerResDto;
import com.logistics.delivery.infrastructure.client.dto.DeliveryManagerStatusUpdateReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/delivery-manager")
    DeliveryManagerResDto getDeliveryManager(@RequestParam UUID destinationHubId);

    @PatchMapping("/api/delivery-manager/status")
    void updateDeliveryManagerStatus(@RequestBody DeliveryManagerStatusUpdateReqDto request);
}
