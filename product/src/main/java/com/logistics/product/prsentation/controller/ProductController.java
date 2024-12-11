package com.logistics.product.prsentation.controller;

import com.logistics.product.application.dto.ApiResponse;
import com.logistics.product.application.dto.ProductReqDto;
import com.logistics.product.application.dto.ProductResDto;
import com.logistics.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vi")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ROLE_MASTER_ADMIN') or hasRole('ROLE_HUB_ADMIN')")
    @PostMapping("/products")
    public ApiResponse<ProductResDto> createProduct(@RequestBody ProductReqDto request) {
        return ApiResponse.success("상품 등록", productService.createProduct(request));
    }

}
