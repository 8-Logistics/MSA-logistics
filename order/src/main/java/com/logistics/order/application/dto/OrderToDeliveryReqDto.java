package com.logistics.order.application.dto;


import com.logistics.order.domain.entity.Order;
import lombok.*;

import java.util.UUID;

/* 배송 생성 시 전달 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderToDeliveryReqDto {
    private UUID orderId;
    private UUID sourceHubId; // 출발 허브 ID
    private UUID vendorId; //도착 업체Id
    private String address; //도착 업체 주소
    private String userName;
    private String slackId;

    public static OrderToDeliveryReqDto from(OrderProductResDto productDto,
                                             String productVendorAddress, OrderUserResDto userDto) {
        return new OrderToDeliveryReqDto(
                UUID.randomUUID(),
                productDto.getProductSourceHubId(),
                productDto.getProductVendorId(),
                productVendorAddress,
                userDto.getName(),
                userDto.getSlackId()
        );
    }
}
