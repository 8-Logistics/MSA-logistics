package com.logistics.order.infrastructure.client;

import com.logistics.order.application.config.FeignConfig;
import com.logistics.order.application.dto.OrderDeliveryResDto;
import com.logistics.order.application.dto.OrderToDeliveryReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "delivery-service",configuration = FeignConfig.class)
public interface DeliveryFeignClient {
    @PostMapping("/api/v1/deliveries/createDelivery")
    public OrderDeliveryResDto createDelivery(@RequestBody OrderToDeliveryReqDto dto);
}
