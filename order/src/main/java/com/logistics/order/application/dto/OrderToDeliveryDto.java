package com.logistics.order.application.dto;


import lombok.*;

import java.util.UUID;

/* 배송 생성 시 전달 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderToDeliveryDto {
    private UUID orderId;
    private UUID productSourceHubId;
    private UUID productVendorId;
    private String productVendorAddress;
    private String userName;
    private String slackId;
}
