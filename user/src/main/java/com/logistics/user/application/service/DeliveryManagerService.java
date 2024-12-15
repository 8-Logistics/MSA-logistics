package com.logistics.user.application.service;

import com.logistics.user.application.dto.DeliveryManagerCreateReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;
import com.logistics.user.application.dto.DeliverySequenceDto;

import java.util.UUID;

public interface DeliveryManagerService {


    DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request, String username, String role);

    void deleteDeliveryManager(UUID deliveryId, String username, String role);

    void updateDeliveryStatus(UUID deliveryManagerId, String deliveryStatus);

    DeliverySequenceDto getDeliverySequence(UUID hubId, long deliverySequence);
}
