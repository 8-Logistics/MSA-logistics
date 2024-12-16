package com.logistics.order.infrastructure.client;

import com.logistics.order.application.config.FeignConfig;
import com.logistics.order.application.dto.DeliveryUserResDto;
import com.logistics.order.application.dto.OrderUserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service",configuration = FeignConfig.class)
public interface UserFeignClient {
    @GetMapping("/api/v1/users/userInfo/{userName}")
    OrderUserResDto getUserInfo(@PathVariable("userName") String userName);

    @GetMapping("/api/v1/users/userInfo/{userName}")
    DeliveryUserResDto getDeliveryUserInfo (@PathVariable("userName") String userName);

    @GetMapping("/api/v1/delivery-manager/{deliveryManagerId}/order")
    String getDeliveryManagerUserId(@PathVariable("deliveryManagerId") UUID deliveryManagerId);
}
