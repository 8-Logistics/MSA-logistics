package com.logistics.delivery.application.dto;

import com.logistics.delivery.domain.entity.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DeliveryResDto {

    private UUID id; // 배송 ID
    private UUID orderId; // 주문 ID
    private UUID sourceHubId; // 출발 허브 ID
    private UUID destinationHubId; // 도착 허브 ID
    private String address; // 배송지 주소
    private String recipientName; // 주문인 이름
    private String slackId; // 주문자 슬랙 ID
    private Status status; // 배송 상태
    private LocalDateTime createdAt; // 생성일
    private String createdBy; // 생성자
    private LocalDateTime updatedAt; // 수정일
    private String updatedBy; // 수정자
}
