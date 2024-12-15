package com.logistics.product.prsentation.controller;

import com.logistics.product.application.CustomPrincipal;
import com.logistics.product.application.dto.ApiResponse;
import com.logistics.product.application.dto.OrderProductResDto;
import com.logistics.product.application.dto.ProductReqDto;
import com.logistics.product.application.dto.ProductUpdateReqDto;
import com.logistics.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<UUID> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductUpdateReqDto request,
            @AuthenticationPrincipal CustomPrincipal customPrincipal) {
        UUID updateProductId = productService.updateProduct(productId, request, customPrincipal.getUserId(), customPrincipal.getRole());
        return ApiResponse.success("상품이 수정되었습니다.",updateProductId);
    }

    /**
     * 상품 삭제
     */
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @DeleteMapping("/products/{productId}")
    public ApiResponse deleteProduct(@PathVariable("productId") UUID productId, @AuthenticationPrincipal CustomPrincipal customPrincipal) {
        productService.deleteProduct(productId, customPrincipal.getUserId(), customPrincipal.getRole());
        return ApiResponse.success("상품이 삭제되었습니다.");
    }

    /**
     * Feign Client
     */

    @GetMapping("/products/stock/{productId}")
    public OrderProductResDto getProductInfo(@PathVariable(name="productId") UUID productId){
        return productService.getProductInfo(productId);
    }

    @PutMapping("/products/stockUpdate/{productId}/{stock}")
    public void updateStock(
            @PathVariable(name="productId") UUID productId,
            @PathVariable(name="stock") int stock){
        productService.updateStock(productId, stock);
    }

}
