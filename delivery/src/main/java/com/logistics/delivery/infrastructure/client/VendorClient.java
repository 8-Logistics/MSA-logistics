package com.logistics.delivery.infrastructure.client;

import com.logistics.delivery.infrastructure.client.dto.VendorResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vendor-service")
public interface VendorClient {

    @GetMapping("/api/v1/vendors/{vendorId}")
    VendorResDto getVendor(@PathVariable("vendorId") UUID vendorId);
}
