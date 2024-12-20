package com.logistics.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryManagerType {

    HUB_DELIVERY("HUB_DELIVERY"),
    VENDOR_DELIVERY("VENDOR_DELIVERY");

    private final String description;

}
