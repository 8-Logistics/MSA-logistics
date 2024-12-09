package com.logistics.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    PENDING("보류"),
    ACCEPTED("수락"),
    REJECTED("거절"),
    NONE("없음");

    private final String description;
}
