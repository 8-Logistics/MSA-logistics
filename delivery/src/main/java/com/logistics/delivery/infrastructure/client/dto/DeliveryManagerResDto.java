package com.logistics.delivery.infrastructure.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class DeliveryManagerResDto {

    private UUID deliveryManagerId; // 배송 관리자 ID
    private int sequence; // 우선순위
}
