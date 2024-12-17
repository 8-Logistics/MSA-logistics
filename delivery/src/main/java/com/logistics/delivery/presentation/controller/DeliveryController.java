package com.logistics.delivery.presentation.controller;

import com.logistics.delivery.application.dto.*;
import com.logistics.delivery.application.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Delivery-Service API", description = "배송 API Controller")
@RequestMapping("/api/v1")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "배송 생성 API")
    @PostMapping("/deliveries")
    @PreAuthorize("hasAnyAuthority('MASTER')")
    public ResponseEntity<DeliveryCreateResDto> createDelivery(@RequestBody @Valid DeliveryCreateReqDto request) {

        DeliveryCreateResDto response = deliveryService.createDelivery(request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "배송 조회 API")
    @PatchMapping("/deliveries/{deliveryId}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER','DELIVERY_MANAGER')")
    public ApiResponse<DeliveryUpdateResDto> updateDeliveryStatus(
            @PathVariable UUID deliveryId,
            @RequestBody @Valid DeliveryUpdateReqDto request) {
        DeliveryUpdateResDto response = deliveryService.updateDeliveryStatus(deliveryId, request);
        return ApiResponse.success("배송 조회 성공", response);
    }

    @Operation(summary = "배송 삭제 API")
    @DeleteMapping("/deliveries/{deliveryId}")
    @PreAuthorize("hasAnyAuthority('MASTER')")
    public ApiResponse<String> deleteDelivery(@PathVariable UUID deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return ApiResponse.success("배송 삭제 성공");
    }

    @Operation(summary = "배송 단건 조회 API")
    @GetMapping("/deliveries/{deliveryId}")
    public ApiResponse<DeliveryResDto> getDelivery(@PathVariable UUID deliveryId) {
        DeliveryResDto delivery = deliveryService.getDeliveryById(deliveryId);
        return ApiResponse.success("배송 조회 성공", delivery);
    }

    @Operation(summary = "배송 조회/검색 API")
    @GetMapping("/deliveries")
    public ApiResponse<Page<DeliveryResDto>> getDeliveries(
            @RequestParam(required = false, defaultValue = "recipientName") String condition, // 기준이 애매해서 default 수령인 기준 검색
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "false") boolean isAsc) {
        Page<DeliveryResDto> deliveries = deliveryService.getDeliveries(condition, keyword, status, pageNumber, isAsc);
        return ApiResponse.success("배송 목록 죄회 성공", deliveries);
    }

    @Operation(summary = "배송 경로 조회 API")
    @GetMapping("/deliveryPaths/{deliveryPathId}")
    public ApiResponse<DeliveryPathResDto> getDeliveryPath(@PathVariable UUID deliveryPathId) {
        DeliveryPathResDto deliveryPath = deliveryService.getDeliveryPathById(deliveryPathId);
        return ApiResponse.success("배송 경로 조회 성공", deliveryPath);
    }
}
