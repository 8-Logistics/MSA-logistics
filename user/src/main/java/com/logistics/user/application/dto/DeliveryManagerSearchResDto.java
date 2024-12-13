package com.logistics.user.application.dto;

import com.logistics.user.domain.entity.DeliveryManager;
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
    private String sourceHubId;
    private Integer deliverySequence;
    private String deliveryStatus;


    public static DeliveryManagerSearchResDto toResponse(DeliveryManager deliveryManager) {
        return DeliveryManagerSearchResDto.builder()
                .deliveryManagerId(deliveryManager.getDeliveryManagerId().toString())
                .deliveryManagerType(deliveryManager.getDeliveryManagerType().toString())
                .sourceHubId(deliveryManager.getSourceHubId().toString())
                .deliverySequence(deliveryManager.getDeliverySequence())
                .deliveryStatus(deliveryManager.getDeliveryStatus().toString())
                .build();
    }

}
