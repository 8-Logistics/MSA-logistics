package com.logistics.product.prsentation.controller;

import com.logistics.product.application.CustomPrincipal;
import com.logistics.product.application.dto.*;
import com.logistics.product.application.service.ProductService;
import com.logistics.product.domain.entity.Product;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
     * 상품 단건 조회
     */
    @GetMapping("/products/{productId}")
    public ApiResponse<ProductResDto> retrieveProduct(@PathVariable("productId") UUID productId) {
        return ApiResponse.success("상품이 조회되었습니다.", productService.retrieveProduct(productId));
    }

    /**
     * 상품 검색
     */
    @GetMapping("/products/search")
    public ApiResponse<ProductSearchResDto> searchProduct(
            @RequestParam(required = false) List<UUID> idList,
            @QuerydslPredicate(root = Product.class) Predicate predicate,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ApiResponse.success("검색하신 상품 조회되었습니다.", productService.searchProduct(idList, predicate, pageable));
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
