package com.logistics.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerSearchResDto {

    private String deliveryManagerId;
    private String deliveryManagerType;
    private long userId;
    private String sourceHubId;
    private Integer deliverySequence;

}
