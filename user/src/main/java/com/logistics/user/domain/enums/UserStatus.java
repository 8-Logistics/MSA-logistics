package com.logistics.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    PENDING_DELIVERY_MANAGER("배송 담당자 승인 보류"),
    PENDING_VENDOR_MANAGER("업체 담당자 승인 보류"),
    PENDING_HUB_MANAGER("허브 담당자 승인 보류"),

    ACCEPTED_HUB_MANAGER("허브 담당자 승인 완료"),
    ACCEPTED_DELIVERY_MANAGER("배송 담당자 승인 완료"),
    ACCEPTED_VENDOR_MANAGER("업체 담당자 승인 완료"),

    REJECTED_HUB_MANAGER("허브 담당자 승인 거절"),
    REJECTED_DELIVERY_MANAGER("배송 담당자 승인 거절"),
    REJECTED_VENDOR_MANAGER("업체 담당자 승인 거절"),

    NONE("없음");

    private final String description;
}
