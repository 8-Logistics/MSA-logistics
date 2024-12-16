package com.logistics.delivery.infrastructure.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class HubPathResDto {

    private UUID pathId; // 이동 경로 UUID
    private UUID sourceHubId; // 출발 허브 UUID
    private UUID destinationHubId; // 도착 허브 UUID
    private Double distance; // 이동 거리
    private LocalTime estimatedTime; // 예상 소요 시간
}