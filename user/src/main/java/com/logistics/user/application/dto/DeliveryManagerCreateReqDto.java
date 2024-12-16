package com.logistics.user.application.dto;

import com.logistics.user.domain.enums.DeliveryManagerType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerCreateReqDto {

    private DeliveryManagerType deliveryManagerType;
    private Long userId;
    private UUID sourceHubId;
}
