package com.logistics.delivery.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DeliveryResDto {

    private final UUID deliveryId;
    private final UUID orderId;
    private final UUID deliveryPathId;
}

