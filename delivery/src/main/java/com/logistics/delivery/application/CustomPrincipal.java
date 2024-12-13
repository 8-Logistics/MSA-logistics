package com.logistics.delivery.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomPrincipal {

    private final String userId;
    private final String role;

    public static CustomPrincipal createPrinciple(String userId, String role) {
        return CustomPrincipal.builder()
                .userId(userId)
                .role(role).build();
    }

}
