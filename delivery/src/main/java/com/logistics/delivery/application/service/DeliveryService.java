package com.logistics.delivery.application.service;

import com.logistics.delivery.application.dto.DeliveryCreateReqDto;
import com.logistics.delivery.application.dto.DeliveryResDto;
import com.logistics.delivery.domain.entity.Delivery;
import com.logistics.delivery.domain.entity.DeliveryPath;
import com.logistics.delivery.domain.entity.Status;
import com.logistics.delivery.domain.repository.DeliveryRepository;
import com.logistics.delivery.infrastructure.client.HubClient;
import com.logistics.delivery.infrastructure.client.UserClient;
import com.logistics.delivery.infrastructure.client.VendorClient;
import com.logistics.delivery.infrastructure.client.dto.DeliveryManagerResDto;
import com.logistics.delivery.infrastructure.client.dto.HubPathResDto;
import com.logistics.delivery.infrastructure.client.dto.VendorResDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;
    private final UserClient userClient;
    private final VendorClient vendorClient;

    // 허브 경로 정보 가져오기
    public HubPathResDto getHubPath(UUID sourceHubId, UUID destinationHubId) {
        return hubClient.getHubPath(sourceHubId, destinationHubId);
    }

    // 업체 담당 허브 정보 가져오기
    public VendorResDto getVendor(UUID vendorId) {
        return vendorClient.getVendor(vendorId);
    }

    // 허브 배송 담당자 정보 가져오기
    public DeliveryManagerResDto getHubDeliveryManager() {
        return userClient.getDeliveryManager(null); // destinationHubId 없이 호출
    }

    // 업체 배송 담당자 정보 가져오기
    public DeliveryManagerResDto getVendorDeliveryManager(UUID destinationHubId) {
        return userClient.getDeliveryManager(destinationHubId);
    }

    @Transactional
    public DeliveryResDto createDelivery(String createdBy, DeliveryCreateReqDto request) {
        // 도착 허브 ID 가져오기
        UUID destinationHubId = getVendor(request.getVendorId()).getVendorHubId();

        //  허브 배송 담당자 정보 가져오기
        DeliveryManagerResDto hubManager = getHubDeliveryManager();

        // 허브 경로 정보 가져오기
        HubPathResDto hubPath = getHubPath(request.getSourceHubId(), destinationHubId);

        // 배송 엔티티 생성
        Delivery delivery = Delivery.builder()
                .orderId(request.getOrderId())
                .sourceHubId(request.getSourceHubId())
                .destinationHubId(destinationHubId)
                .address(request.getAddress())
                .recipientName(request.getRecipientName())
                .slackId(request.getSlackId())
                .status(Status.PENDING)
                .createdBy(createdBy)
                .build();

        // 배송 경로 엔티티 생성
        DeliveryPath deliveryPath = DeliveryPath.builder()
                .hubDeliveryManagerId(hubManager.getDeliveryManagerId())
                .hubDeliveryManagerSequence(hubManager.getSequence())
                .sourceHubId(request.getSourceHubId())
                .destinationHubId(destinationHubId)
                .distance(hubPath.getDistance())
                .estimatedTime(hubPath.getEstimatedTime())
                .status(Status.PENDING)
                .build();

        delivery.addDeliveryPath(deliveryPath);
        deliveryRepository.save(delivery);

        return DeliveryResDto.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .deliveryPathId(deliveryPath.getId())
                .build();
    }
}
