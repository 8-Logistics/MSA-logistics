package com.logistics.order.infrastructure.client;

import com.logistics.order.application.dto.OrderCreateReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient("delivery-delivery")
public interface DeliveryFeignClient {
    @PostMapping("/api/v1/deliveries/createDelivery")
    public UUID createDelivery(@RequestBody OrderCreateReqDto dto);
}
