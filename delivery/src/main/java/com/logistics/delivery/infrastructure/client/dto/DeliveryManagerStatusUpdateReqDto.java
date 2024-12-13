package com.logistics.delivery.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryManagerStatusUpdateReqDto {
    private UUID deliveryManagerId; // 배송 담당자 ID
    private String status; // 배송 상태 (배송중, 배송대기)
}
