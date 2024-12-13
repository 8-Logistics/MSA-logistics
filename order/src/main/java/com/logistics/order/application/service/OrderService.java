package com.logistics.order.application.service;

import com.logistics.order.application.dto.*;
import com.logistics.order.domain.entity.Order;
import com.logistics.order.domain.repository.OrderRepository;
import com.logistics.order.infrastructure.client.DeliveryFeignClient;
import com.logistics.order.infrastructure.client.ProductFeignClient;
import com.logistics.order.infrastructure.client.UserFeignClient;
import com.logistics.order.infrastructure.client.VendorFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private VendorFeignClient vendorFeignClient;

    @Transactional
    public OrderCreateResDto createOrder(OrderCreateReqDto request, String userId) {
        Order order = OrderCreateReqDto.toOrder(request);
        int quantity = request.getQuantity();

        OrderProductDto orderProductDto = productFeignClient.getProductInfo(order.getProductId());

        if(orderProductDto.getStock() >= quantity) {
            productFeignClient.updateStock(order.getProductId(), orderProductDto.getStock() - quantity);
        }else {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        // productVendorId로 vendor에 요청 > vendorId(업체) 주소 받기
        String productVendorAddress = vendorFeignClient.getVendorAddress(orderProductDto.getProductVendorId());

        // userId로 user에 배송담당자에 요청 > userId로 유저 이름, slackId 받기
        OrderUserDto orderUserDto = userFeignClient.getUserInfo(userId);


        OrderToDeliveryDto orderToDeliveryDto = new OrderToDeliveryDto(
                order.getOrderId(),
                orderProductDto.getProductSourceHubId(),
                orderProductDto.getProductVendorId(),
                productVendorAddress,
                orderUserDto.getUserName(),
                orderUserDto.getSlackId());

        UUID deliveryId;

        try {
            deliveryId = deliveryFeignClient.createDelivery(orderToDeliveryDto);
            order = order.createOrder(order, orderProductDto, userId, deliveryId);

        }catch (Exception e) {
            // 오류 발생 시 기존 재고로 재업데이트
            productFeignClient.updateStock(order.getProductId(), orderProductDto.getStock());
            throw new IllegalArgumentException("배송 등록 중 오류가 발생하였습니다.");
        }

        // Todo 슬랙 서비스 호출
        return OrderCreateResDto.from(orderRepository.save(order));
    }
}
