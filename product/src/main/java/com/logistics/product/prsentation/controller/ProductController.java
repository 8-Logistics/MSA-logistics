package com.logistics.product.prsentation.controller;

import com.logistics.product.application.dto.ApiResponse;
import com.logistics.product.application.dto.ProductReqDto;
import com.logistics.product.application.dto.ProductUpdateReqDto;
import com.logistics.product.application.service.ProductService;
import com.logistics.product.domain.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyAuthority('HUB_MANAGER', 'VENDOR_MANAGER', 'MASTER')")
    @PostMapping("/products")
    public ApiResponse<UUID> createProduct(@RequestBody ProductReqDto request) {
        UUID productId = productService.createProduct(request);
        return ApiResponse.success("상품을 등록하였습니다.", productId);
    }

    /**
     * 상품 수정
     */
    @PreAuthorize("hasAnyAuthority('HUB_MANAGER', 'VENDOR_MANAGER', 'MASTER')")
    @PutMapping("/products/{productId}")
    public ApiResponse<UUID> updateProduct(@PathVariable UUID productId, @RequestBody ProductUpdateReqDto request) {
        String role = "HUB_ADMIN";
        String userId = "123";
        UUID updateProductId = productService.updateProduct(productId, request, userId, role);
        return ApiResponse.success("상품이 수정되었습니다.",updateProductId);
    }

    /**
     * 상품 삭제
     */
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @DeleteMapping("/products/{productId}")
    public ApiResponse deleteProduct(@PathVariable("productId") UUID productId) {
        String role = "HUB_ADMIN";
        String userId = "123";
        productService.deleteProduct(productId, userId, role);
        return ApiResponse.success("상품이 삭제되었습니다.");
    }


}
