package com.logistics.order.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveryResDto {
    private UUID deliveryId;
    private UUID orderId;
    private String sourceHubAddress; // 출발 허브 주소
    private UUID hubDeliveryManagerId;
}
