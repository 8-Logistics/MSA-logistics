package com.logistics.product.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vendor-service")
public interface VendorFeignClient {
    // Todo feign 완성 시 테스트 필요
    /* 상품 등록 시 등록 업체가 존재하는지 확인
     * vendorId가 있는지 없는지 체크 = return true/false */
    @GetMapping("/api/v1/vendors/vendor/{vendorId}")
    public boolean checkVendor(@PathVariable UUID vendorId);

    /* 상품 수정 시 업체 매니저인 경우 vendor ID를 가져와
    상품의 vendor ID와 같은지 체크하기 위함 = return userId의 vendorId*/
    @GetMapping("/api/v1/vendors/user/{userId}")
    public UUID getUserVendorId(String userId);
}
