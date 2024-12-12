package com.logistics.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryManagerType {

    HUB_DELIVERY("hub_delivery"),
    VENDOR_DELIVERY("vendor_delivery");

    private final String description;

}
