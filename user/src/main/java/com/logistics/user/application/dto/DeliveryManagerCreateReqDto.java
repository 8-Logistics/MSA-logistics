package com.logistics.user.application.dto;

import com.logistics.user.domain.enums.DeliveryManagerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerCreateReqDto {

    private DeliveryManagerType deliveryManagerType;
    private Long userId;
    private UUID sourceHubId;
}
