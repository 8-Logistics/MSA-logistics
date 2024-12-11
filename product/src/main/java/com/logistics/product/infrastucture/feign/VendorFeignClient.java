package com.logistics.product.infrastucture.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vendor-service")
public interface VendorFeignClient {
    @GetMapping("api/vi/vendor/{vendorId}")
    public boolean checkVendor(@PathVariable UUID vendorId);
}
