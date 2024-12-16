package com.logistics.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderUserDto {

    private String name;
    private String slackId;
    private String email;

    public static OrderUserDto toResponse(String name, String slackId, String email) {
        return OrderUserDto.builder()
                .name(name)
                .slackId(slackId)
                .email(email)
                .build();
    }
}
