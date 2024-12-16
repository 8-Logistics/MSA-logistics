package com.logistics.delivery.domain.entity;

import lombok.Getter;

@Getter
public enum Status { // 배송 상태
    PENDING("허브 대기 중"),
    MOVING_TO_HUB("허브 이동 중"),
    DELIVERED_TO_HUB("목적지 허브 도착"),
    MOVING_TO_VENDOR("업체 이동 중"),
    DELIVERED_TO_VENDOR("배송 완료");

    private final String description;

    Status(String description) {
        this.description = description;
    }
}
