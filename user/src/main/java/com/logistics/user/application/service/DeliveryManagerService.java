package com.logistics.user.application.service;

import com.logistics.user.application.dto.DeliveryManagerCreateReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;

public interface DeliveryManagerService {


    DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request);
}
