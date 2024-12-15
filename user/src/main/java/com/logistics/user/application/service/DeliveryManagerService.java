package com.logistics.user.application.service;

import com.logistics.user.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeliveryManagerService {


    DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request, String username, String role);

    void deleteDeliveryManager(UUID deliveryId, String username, String role);

    void updateDeliveryStatus(UUID deliveryManagerId, String deliveryStatus);

    DeliverySequenceDto getDeliverySequence(UUID hubId, long deliverySequence);

    DeliveryManagerSearchResDto getDeliveryManager(UUID deliveryManagerId);

    DeliveryManagerSearchResDto modifyDeliveryManager(UUID deliveryManagerId, DeliveryManagerUpdateReqDto request
                , String username, String role);

    Page<DeliveryManagerSearchResDto> getDeliveryManagerSearch(DeliveryManagerSearchReqDto request, Pageable pageable
                , String username, String role);
}
