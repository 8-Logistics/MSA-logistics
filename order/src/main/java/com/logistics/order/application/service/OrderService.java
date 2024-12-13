package com.logistics.order.application.service;

import com.logistics.order.application.CustomPrincipal;
import com.logistics.order.application.dto.OrderCreateReqDto;
import com.logistics.order.application.dto.OrderCreateResDto;
import com.logistics.order.application.dto.OrderProductDto;
import com.logistics.order.domain.entity.Order;
import com.logistics.order.domain.repository.OrderRepository;
import com.logistics.order.infrastructure.client.DeliveryFeignClient;
import com.logistics.order.infrastructure.client.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
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

    @Transactional
    public OrderCreateResDto createOrder(OrderCreateReqDto request, String userId) {
        Order order = OrderCreateReqDto.toOrder(request);
        int quantity = request.getQuantity();

        OrderProductDto orderProductDto = productFeignClient.getStock(order.getProductId());

        if(orderProductDto.getStock() >= quantity) {
            productFeignClient.decreaseStock(order.getProductId(), orderProductDto.getStock() - quantity);
        }else {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        //UUID deliveryId = deliveryFeignClient.createDelivery(request);
        UUID deliveryId = UUID.fromString("6e0c1b60-b5b8-4c85-bf0c-914f0cb83b24");

        if(Objects.isNull(deliveryId)){
            throw new IllegalArgumentException("배송 생성에 실패했습니다.");
        }
        order = order.createOrder(order, orderProductDto, userId, deliveryId);
        return OrderCreateResDto.from(orderRepository.save(order));
    }
}
