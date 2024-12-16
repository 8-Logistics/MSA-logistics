package com.logistics.order.domain.entity;

import com.logistics.order.application.dto.OrderDeliveryResDto;
import com.logistics.order.application.dto.OrderProductResDto;
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

    @Column
    private String recipientName;

    @Column
    private int quantity;

    @Column
    private String pickupRequest;

    public Order createOrder(Order order, OrderProductResDto orderProductResDto, String userId, OrderDeliveryResDto orderDeliveryResDto) {
        return Order.builder()
                .deliveryId(orderDeliveryResDto.getDeliveryId())
                .providerVendorId(orderProductResDto.getProductVendorId())
                .receiveVendorId(order.getReceiveVendorId())
                .productId(order.getProductId())
                .recipientName(userId)
                .quantity(order.getQuantity())
                .pickupRequest(order.getPickupRequest())
                .build();

    }

}
