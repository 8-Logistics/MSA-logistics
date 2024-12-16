package com.logistics.user.application.dto;

import com.logistics.user.domain.enums.DeliveryManagerType;
import com.logistics.user.domain.enums.DeliveryStatus;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerSearchReqDto {

    private DeliveryManagerType deliveryManagerType;
    private DeliveryStatus deliveryStatus;
    private List<UUID> hubIdList;
    private Long maxSequence;
    private Long minSequence;

}
