package com.logistics.order.presentation.controller;

import com.logistics.order.application.CustomPrincipal;
import com.logistics.order.application.dto.ApiResponse;
import com.logistics.order.application.dto.OrderCreateReqDto;
import com.logistics.order.application.dto.OrderCreateResDto;
import com.logistics.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     */
    @PostMapping("/orders")
    public ApiResponse<OrderCreateResDto> createOrder(
            @RequestBody OrderCreateReqDto request, @AuthenticationPrincipal CustomPrincipal customPrincipal
    ) {
        return ApiResponse.success("주문이 완료되었습니다.", orderService.createOrder(request,customPrincipal.getUserId()));
    }
}
