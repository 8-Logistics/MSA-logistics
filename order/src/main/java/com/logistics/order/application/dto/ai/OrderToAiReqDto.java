package com.logistics.order.application.dto.ai;

import com.logistics.order.application.dto.DeliveryUserResDto;
import com.logistics.order.application.dto.OrderDeliveryResDto;
import com.logistics.order.application.dto.OrderToDeliveryReqDto;
import com.logistics.order.application.dto.OrderUserResDto;
import com.logistics.order.domain.entity.Order;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderToAiReqDto {
    private UUID orderId;
    private String userName;
    private String userEmail;
    private String productName;
    private int stock;
    private String pickupRequest;
    private String sourceHubAddress; // 출발 허브 주소
    private String productVendorAddress; // 도착지(수령업체)
    private String deliveryManagerName;
    private String deliveryManagerEmail;
    private String deliveryManagerSlackId;

    public static OrderToAiReqDto from(Order order, OrderUserResDto orderUserDto, String productVendorAddress,
                                       OrderDeliveryResDto orderDeliveryResDto, DeliveryUserResDto deliveryUserResDto, String productName) {
        return OrderToAiReqDto.builder()
                .orderId(order.getOrderId())
                .userName(orderUserDto.getUserName())
                .userEmail(orderUserDto.getEmail())
                .productName(productName)
                .stock(order.getQuantity())
                .pickupRequest(order.getPickupRequest())
                .sourceHubAddress(orderDeliveryResDto.getSourceHubAddress())
                .productVendorAddress(productVendorAddress)
                .deliveryManagerName(deliveryUserResDto.getUserName())
                .deliveryManagerEmail(deliveryUserResDto.getEmail())
                .deliveryManagerSlackId(orderUserDto.getSlackId())
                .build();

    }
}
