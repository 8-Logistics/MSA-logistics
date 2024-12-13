package com.logistics.delivery.application.dto;

import com.logistics.delivery.domain.entity.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
public class DeliveryPathResDto {

    private UUID id; // 배송 경로 ID
    private UUID hubDeliveryManagerId; // 허브 배송 담당자 ID
    private int hubDeliveryManagerSequence; // 허브 배송 담당자 시퀀스
    private UUID vendorDeliveryManagerId; // 업체 배송 담당자 ID
    private int vendorDeliveryManagerSequence; // 업체 배송 담당자 시퀀스
    private UUID sourceHubId; // 출발 허브 ID
    private UUID destinationHubId; // 도착 허브 ID
    private double distance; // 예상 거리
    private LocalTime estimatedTime; // 예상 소요 시간
    private Status status; // 배송 상태
    private LocalDateTime createdAt; // 생성 시간
    private String createdBy; // 생성자
    private LocalDateTime updatedAt; // 수정 시간
    private String updatedBy; // 수정자
}
