package com.logistics.product.prsentation.controller;

import com.logistics.product.application.CustomPrincipal;
import com.logistics.product.application.dto.*;
import com.logistics.product.application.service.ProductService;
import com.logistics.product.domain.entity.Product;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name="Product", description="Product API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary="상품 추가", description="업체에 상품을 추가합니다.")
    @PreAuthorize("hasAnyAuthority('HUB_MANAGER', 'VENDOR_MANAGER', 'MASTER')")
    @PostMapping("/products")
    public ApiResponse<UUID> createProduct(@RequestBody ProductReqDto request) {
        UUID productId = productService.createProduct(request);
        return ApiResponse.success("상품을 등록하였습니다.", productId);
    }

    /**
     * 상품 수정
     */
    @Operation(summary="상품 수정", description="상품의 정보를 수정합니다.")
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
    @Operation(summary="상품 삭제", description="특정 상품을 삭제합니다.")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @DeleteMapping("/products/{productId}")
    public ApiResponse deleteProduct(@PathVariable("productId") UUID productId, @AuthenticationPrincipal CustomPrincipal customPrincipal) {
        productService.deleteProduct(productId, customPrincipal.getUserId(), customPrincipal.getRole());
        return ApiResponse.success("상품이 삭제되었습니다.");
    }

    /**
     * 상품 단건 조회
     */
    @Operation(summary="상품 상세 조회", description="특정 상품을 조회합니다.")
    @GetMapping("/products/{productId}")
    public ApiResponse<ProductResDto> retrieveProduct(@PathVariable("productId") UUID productId) {
        return ApiResponse.success("상품이 조회되었습니다.", productService.retrieveProduct(productId));
    }

    /**
     * 상품 검색
     */
    @Operation(summary="상품 검색", description="상품 전체 목록 중 원하는 키워드로 검색합니다.")
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
