package com.logistics.order.application.service;

import com.logistics.order.application.dto.*;
import com.logistics.order.application.dto.ai.OrderToAiReqDto;
import com.logistics.order.domain.entity.Order;
import com.logistics.order.domain.repository.OrderRepository;
import com.logistics.order.infrastructure.client.DeliveryFeignClient;
import com.logistics.order.infrastructure.client.ProductFeignClient;
import com.logistics.order.infrastructure.client.UserFeignClient;
import com.logistics.order.infrastructure.client.VendorFeignClient;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.logistics.order.domain.entity.QOrder.order;

import java.util.List;
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
    private SlackService slackService;

    @Transactional
    public OrderCreateResDto createOrder(OrderCreateReqDto request, String userId) {
        Order order = OrderCreateReqDto.toOrder(request);
        int quantity = request.getQuantity();

        OrderProductResDto orderProductResDto = productFeignClient.getProductInfo(order.getProductId());

        if(orderProductResDto.getStock() >= quantity) {
            productFeignClient.updateStock(order.getProductId(), orderProductResDto.getStock() - quantity);
        }else {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        // productVendorId로 vendor에 요청 > vendorId(업체) 주소 받기
        String productVendorAddress = vendorFeignClient.getVendorAddress(orderProductResDto.getProductVendorId());

        // deliveryId로 user > user 이름, 메일 주소 가져오기
        OrderUserResDto orderUserDto = userFeignClient.getUserInfo(userId);

        OrderToDeliveryReqDto orderToDeliveryReqDto = OrderToDeliveryReqDto.from(order, orderProductResDto, productVendorAddress, orderUserDto);


        try {
            OrderDeliveryResDto orderDeliveryResDto = deliveryFeignClient.createDelivery(orderToDeliveryReqDto);

            String deliveryManagerId = userFeignClient.getDeliveryManagerUserId(orderDeliveryResDto.getDeliveryManagerId());
            DeliveryUserResDto deliveryUserResDto = userFeignClient.getDeliveryUserInfo(deliveryManagerId);
            order = order.createOrder(order, orderProductResDto, userId, orderDeliveryResDto);

            OrderToAiReqDto orderToAiReqDto = OrderToAiReqDto.from(order, orderUserDto, productVendorAddress,
                    orderDeliveryResDto, deliveryUserResDto, orderProductResDto.getProductName());

            slackService.sendSlack(orderToAiReqDto);

        }catch (Exception e) {
            // 오류 발생 시 기존 재고로 재업데이트
            productFeignClient.updateStock(order.getProductId(), orderProductResDto.getStock());
            throw new IllegalArgumentException("배송 등록 중 오류가 발생하였습니다.");
        }

        return OrderCreateResDto.from(orderRepository.save(order));
    }

    public OrderRetrieveResDto retrieveOrder(UUID orderId) {
        Order order = (Order) orderRepository.findByOrderIdAndIsDeleteFalse(orderId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return OrderRetrieveResDto.from(order);
    }

    public OrderSearchResDto searchOrder(List<UUID> idList, Predicate predicate, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);
        if (idList != null && !idList.isEmpty()) {
            booleanBuilder.and(order.orderId.in(idList));
        }
        booleanBuilder.and(order.isDelete.eq(false));
        Page<Order> orderEntityPage = orderRepository.findAll(booleanBuilder, pageable);
        return OrderSearchResDto.of(orderEntityPage);
    }
}
