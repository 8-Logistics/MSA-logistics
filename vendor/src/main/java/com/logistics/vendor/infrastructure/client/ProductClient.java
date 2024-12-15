package com.logistics.vendor.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.logistics.vendor.application.service.ProductService;
import com.logistics.vendor.infrastructure.config.FeignConfig;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient extends ProductService {
	// 업체 삭제 시 해당업체의 상품삭제도 하기 위한 호출
	@DeleteMapping("/api/v1/products/")
	void deleteProduct(@RequestParam("vendorId") UUID vendorId);

}
