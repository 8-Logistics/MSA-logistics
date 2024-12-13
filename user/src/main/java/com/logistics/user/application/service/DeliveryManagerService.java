package com.logistics.user.application.service;

import com.logistics.user.application.dto.DeliveryManagerCreateReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;

import java.util.UUID;

public interface DeliveryManagerService {


    DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request);

    void deleteDeliveryManager(UUID deliveryId);
}
