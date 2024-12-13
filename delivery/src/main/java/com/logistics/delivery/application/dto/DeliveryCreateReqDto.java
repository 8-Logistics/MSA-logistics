package com.logistics.delivery.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class DeliveryCreateReqDto {

    @NotNull
    private final UUID orderId; // 주문 ID

    @NotNull
    private final UUID sourceHubId; // 출발 허브 ID

    @NotNull
    private final UUID vendorId; // 도착 업체 id
    @NotBlank
    private final String address; // 배송지 주소

    @NotBlank
    private final String recipientName; // 수령인 이름

    @NotBlank
    private final String slackId; // 수령인의 Slack ID

    // 배송 경로에 필요한 필드
    @NotNull
    private final UUID hubDeliveryManagerId; // 허브 배송 담당자 ID
}
