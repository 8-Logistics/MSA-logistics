package com.logistics.delivery.presentation.controller;

import com.logistics.delivery.application.dto.DeliveryCreateReqDto;
import com.logistics.delivery.application.dto.DeliveryResDto;
import com.logistics.delivery.application.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/deliveries")
    public ResponseEntity<DeliveryResDto> createDelivery(
            @RequestHeader("username") String username,
            @RequestHeader("role") String role,
            @RequestBody @Valid DeliveryCreateReqDto request) {

        if (!"MASTER".equals(role)) {
            throw new IllegalStateException("관리자만 사용 가능한 기능입니다.");
        }

        DeliveryResDto response = deliveryService.createDelivery(username, request);

        return ResponseEntity.ok(response);
    }
}
