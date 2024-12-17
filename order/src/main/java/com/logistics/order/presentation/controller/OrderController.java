package com.logistics.order.presentation.controller;

import com.logistics.order.application.CustomPrincipal;
import com.logistics.order.application.dto.*;
import com.logistics.order.application.service.OrderService;
import com.logistics.order.domain.entity.Order;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="Order", description="Order API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     */
    @Operation(summary="주문 생성", description="새 주문을 생성합니다.")
    @PostMapping("/orders")
    public ApiResponse<OrderCreateResDto> createOrder(
            @RequestBody OrderCreateReqDto request, @AuthenticationPrincipal CustomPrincipal customPrincipal
    ) {
        return ApiResponse.success("주문이 완료되었습니다.", orderService.createOrder(request,customPrincipal.getUserId()));
    }

    /**
     * 주문 단건 조회
     */
    @Operation(summary="주문 상세 조회", description="특정 주문을 상세 조회합니다.")
    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderRetrieveResDto> retrieveOrder(@PathVariable("orderId")UUID orderId) {
        return ApiResponse.success("주문이 조회되었습니다.", orderService.retrieveOrder(orderId));
    }

    /**
     * 주문 검색 (전체 조회)
     */
    @Operation(summary="주문 전체 조회(검색)", description="주문을 전체 조회 or 키워드 검색이 가능합니다.")
    @GetMapping("/orders/search")
    public ApiResponse<OrderSearchResDto> searchProduct(
            @RequestParam(required = false) List<UUID> idList,
            @QuerydslPredicate(root = Order.class) Predicate predicate,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ApiResponse.success("주문이 조회되었습니다.", orderService.searchOrder(idList, predicate, pageable));
    }

}
