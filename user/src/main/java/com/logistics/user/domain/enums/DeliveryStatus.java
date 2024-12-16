package com.logistics.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    IN_DELIVERY("IN_DELIVERY"), // 배송중
    PENDING_DELIVERY("PENDING_DELIVERY"); // 배송대기

    private final String description;

}
