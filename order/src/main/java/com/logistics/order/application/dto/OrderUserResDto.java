package com.logistics.order.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUserResDto {
    private String name;
    private String slackId;
    private String email;
}
