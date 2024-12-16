package com.logistics.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DeliverySequenceDto {

    private UUID DeliveryManagerId;
    private long deliverySequence;

    public static DeliverySequenceDto toResponse(UUID deliveryManagerId, long deliverySequence) {
        return DeliverySequenceDto.builder()
                .DeliveryManagerId(deliveryManagerId)
                .deliverySequence(deliverySequence)
                .build();
    }
}
