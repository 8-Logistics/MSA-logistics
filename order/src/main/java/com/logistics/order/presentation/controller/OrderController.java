package com.logistics.order.presentation.controller;

import com.logistics.order.application.CustomPrincipal;
import com.logistics.order.application.dto.*;
import com.logistics.order.application.service.OrderService;
import com.logistics.order.application.service.SlackService;
import com.logistics.order.domain.entity.Order;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    /**
     * 주문 단건 조회
     */
    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderRetrieveResDto> retrieveOrder(@PathVariable("orderId")UUID orderId) {
        return ApiResponse.success("주문이 조회되었습니다.", orderService.retrieveOrder(orderId));
    }

    /**
     * 주문 검색 (전체 조회)
     */
    @GetMapping("/orders/search")
    public ApiResponse<OrderSearchResDto> searchProduct(
            @RequestParam(required = false) List<UUID> idList,
            @QuerydslPredicate(root = Order.class) Predicate predicate,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ApiResponse.success("주문이 조회되었습니다.", orderService.searchOrder(idList, predicate, pageable));
    }

}
