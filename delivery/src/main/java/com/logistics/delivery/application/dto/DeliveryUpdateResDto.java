package com.logistics.delivery.application.dto;

import com.logistics.delivery.domain.entity.Status;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DeliveryUpdateResDto {

    private Status status; // 변경된 배송 상태
    private UUID currentDeliveryManagerId; // 현재 배송자 ID
}
