package com.logistics.delivery.infrastructure.client;

import com.logistics.delivery.infrastructure.client.dto.DeliveryManagerResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/v1/delivery-manager")
    DeliveryManagerResDto getDeliveryManager(@RequestParam UUID destinationHubId);

    @PutMapping("/api/v1/delivery-manager/{deliveryManagerId}/updateStatus")
    void updateDeliveryManagerStatus(@PathVariable UUID deliveryManagerId,
                                     @RequestParam("deliveryStatus") String deliveryStatus);
}
