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

    public static OrderUserDto toResponse(String name, String slackId) {
        return OrderUserDto.builder()
                .name(name)
                .slackId(slackId)
                .build();
    }
}
