package com.logistics.order.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUserDto {
    private String userName;
    private String slackId;
}
