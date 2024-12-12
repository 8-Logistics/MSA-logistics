package com.logistics.order.application.dto;

import com.logistics.order.domain.entity.Order;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateResDto {
    private UUID orderId;
    private UUID deliveryId;
    private UUID productId;
    private UUID providerVendorId;
    private UUID receiveVendorId;
    private int quantity;
    private String pickupRequest;
    //private Long recipientId; userId가 String
    private String recipientId;

    public static OrderCreateResDto from(Order order) {
        return OrderCreateResDto.builder()
                .orderId(order.getOrderId())
                .deliveryId(order.getDeliveryId())
                .productId(order.getProductId())
                .providerVendorId(order.getProviderVendorId())
                .receiveVendorId(order.getReceiveVendorId())
                .quantity(order.getQuantity())
                .pickupRequest(order.getPickupRequest())
                .recipientId(order.getRecipientId())
                .build();
    }
}
