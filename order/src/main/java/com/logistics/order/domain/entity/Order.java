package com.logistics.order.domain.entity;

import com.logistics.order.application.dto.OrderProductDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_orders")
public class Order extends BaseEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="order_id")
    private UUID orderId;

    @Column(name="delivery_id")
    private UUID deliveryId;

    @Column
    private UUID providerVendorId;

    @Column
    private UUID receiveVendorId;

    @Column(name="product_id")
    private UUID productId;

    @Column(name = "recipient_id")
    private String recipientId;

    @Column
    private int quantity;

    @Column
    private String pickupRequest;

    public Order createOrder(Order order, OrderProductDto orderProductDto, String userId, UUID deliveryId) {
        return Order.builder()
                .deliveryId(deliveryId)
                .providerVendorId(orderProductDto.getProductVendorId())
                .receiveVendorId(order.getReceiveVendorId())
                .productId(order.getProductId())
                .recipientId(userId)
                .quantity(order.getQuantity())
                .pickupRequest(order.getPickupRequest())
                .build();

    }


    //Todo 배송 완료 시 주문 상태를 바꿀 것인지 (Status) 추가 고려
}
