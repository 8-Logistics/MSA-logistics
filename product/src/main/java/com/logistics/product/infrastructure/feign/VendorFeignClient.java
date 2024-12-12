package com.logistics.product.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vendor-service")
public interface VendorFeignClient {
    @GetMapping("api/v1/vendor/{vendorId}")
    public boolean checkVendor(@PathVariable UUID vendorId);

    @GetMapping("api/v1/vendor/{userId}")
    public UUID getUserVendorId(String userId);
}
