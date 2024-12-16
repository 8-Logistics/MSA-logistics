package com.logistics.delivery.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DeliveryCreateResDto {

    private UUID deliveryId;
    private UUID orderId;
    private UUID deliveryPathId;
    private String sourceHubAddress; // 주소로 변경
    private UUID hubDeliveryManagerId;
}

