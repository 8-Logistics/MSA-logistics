package com.logistics.delivery.presentation.controller;

import com.logistics.delivery.application.dto.*;
import com.logistics.delivery.application.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/deliveries")
    @PreAuthorize("hasRole('MASTER')")
    public ApiResponse<DeliveryCreateResDto> createDelivery(@RequestBody @Valid DeliveryCreateReqDto request) {

        DeliveryCreateResDto response = deliveryService.createDelivery(request);

        return ApiResponse.success("배송 생성 성공", response);
    }

    @PatchMapping("/deliveries/{deliveryId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('HUB_MANAGER') or hasRole('DELIVERY_MANAGER')")
    public ApiResponse<DeliveryUpdateResDto> updateDeliveryStatus(
            @PathVariable UUID deliveryId,
            @RequestBody @Valid DeliveryUpdateReqDto request) {
        DeliveryUpdateResDto response = deliveryService.updateDeliveryStatus(deliveryId, request);
        return ApiResponse.success("배송 조회 성공", response);
    }

    @DeleteMapping("/deliveries/{deliveryId}")
    @PreAuthorize("hasRole('MASTER')")
    public ApiResponse<String> deleteDelivery(@PathVariable UUID deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return ApiResponse.success("배송 삭제 성공");
    }

    @GetMapping("/{deliveryId}")
    public ApiResponse<DeliveryResDto> getDelivery(@PathVariable UUID deliveryId) {
        DeliveryResDto delivery = deliveryService.getDeliveryById(deliveryId);
        return ApiResponse.success("배송 조회 성공", delivery);
    }
}
