package com.logistics.order.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryUserResDto {
    private String userName;
    private String slackId;
    private String email;
}
