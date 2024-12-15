package com.logistics.order.application.dto;

import com.logistics.order.domain.entity.Order;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRetrieveResDto {
    private UUID orderId;
    private UUID deliveryId;
    private UUID productId;
    private String pickupRequest;

    public static OrderRetrieveResDto from(Order order) {
        return OrderRetrieveResDto.builder()
                .orderId(order.getOrderId())
                .deliveryId(order.getDeliveryId())
                .productId(order.getProductId())
                .pickupRequest(order.getPickupRequest())
                .build();
    }
}
