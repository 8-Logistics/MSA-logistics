package com.logistics.user.application.dto;

import com.logistics.user.domain.entity.DeliveryManager;
import com.logistics.user.domain.enums.DeliveryManagerType;
import com.logistics.user.domain.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerSearchResDto {

    private String deliveryManagerId;
    private DeliveryManagerType deliveryManagerType;
    private UUID sourceHubId;
    private Long deliverySequence;
    private DeliveryStatus deliveryStatus;


    public static DeliveryManagerSearchResDto toResponse(DeliveryManager deliveryManager) {
        return DeliveryManagerSearchResDto.builder()
                .deliveryManagerId(deliveryManager.getId().toString())
                .deliveryManagerType(deliveryManager.getDeliveryManagerType())
                .sourceHubId(deliveryManager.getSourceHubId())
                .deliverySequence(deliveryManager.getDeliverySequence())
                .deliveryStatus(deliveryManager.getDeliveryStatus())
                .build();
    }

}
