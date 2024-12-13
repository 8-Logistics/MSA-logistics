package com.logistics.order.infrastructure.client;

import com.logistics.order.application.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vendor-service",configuration = FeignConfig.class)
public interface VendorFeignClient {
    @GetMapping("/api/v1/vendor/{vendorId}")
    String getVendorAddress(@PathVariable("vendorId") UUID vendorId);

}
