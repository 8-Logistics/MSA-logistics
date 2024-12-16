package com.logistics.order.application.dto;

import com.logistics.order.domain.entity.Order;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateReqDto {
    private UUID productId;
    private int quantity;
    private UUID receiveVendorId; //배송 경로 생성을 위해 수령업체 Id 전달
    private String pickupRequest; // 요청사항

    public static Order toOrder(OrderCreateReqDto request) {
        return Order.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .receiveVendorId(request.getReceiveVendorId())
                .pickupRequest(request.getPickupRequest())
                .build();
    }
}
