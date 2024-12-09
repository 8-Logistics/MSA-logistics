package com.logistics.user.domain.enums;

import lombok.Getter;

@Getter
public enum UserRole {

    NORMAL(Authority.NORMAL),  // 사용자 권한
    HUB_MANAGER(Authority.HUB_MANAGER), // 허브 관리자 권한
    DELIVERY_MANAGER(Authority.DELIVERY_MANAGER), // 배송 담당자 권한
    VENDOR_MANAGER(Authority.VENDOR_MANAGER), // 업체 관리자 권한
    MASTER(Authority.MASTER);  // 전체 관리자 권한

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public static class Authority {

        public static final String NORMAL = "ROLE_NORMAL";
        public static final String DELIVERY_MANAGER = "ROLE_DELIVERY_MANAGER";
        public static final String VENDOR_MANAGER = "ROLE_VENDOR_MANAGER";
        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String MASTER = "ROLE_MASTER";
    }
}
