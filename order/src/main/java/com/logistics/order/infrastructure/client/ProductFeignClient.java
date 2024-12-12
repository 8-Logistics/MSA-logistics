package com.logistics.order.infrastructure.client;

import com.logistics.order.application.config.FeignConfig;
import com.logistics.order.application.dto.OrderProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "product-service",configuration = FeignConfig.class)
public interface ProductFeignClient {
    @GetMapping("/api/v1/products/stock/{productId}")
    OrderProductDto getStock(@PathVariable("productId") UUID productId);

    @PutMapping("/api/v1/products/stockUpdate/{productId}/{stock}")
    void decreaseStock(@PathVariable("productId") UUID productId, @PathVariable(name="stock") int stock);
}