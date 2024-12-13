package com.logistics.delivery.application.service;

import com.logistics.delivery.application.dto.DeliveryCreateReqDto;
import com.logistics.delivery.application.dto.DeliveryResDto;
import com.logistics.delivery.application.dto.DeliveryUpdateReqDto;
import com.logistics.delivery.application.dto.DeliveryUpdateResDto;
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
    public HubPathResDto getExactHubPath(UUID sourceHubId, UUID destinationHubId) {
        return hubClient.getExactHubPath(sourceHubId, destinationHubId);
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
    public DeliveryResDto createDelivery(DeliveryCreateReqDto request) {
        // 도착 허브 ID 가져오기
        UUID destinationHubId = getVendor(request.getVendorId()).getVendorHubId();

        //  허브 배송 담당자 정보 가져오기
        DeliveryManagerResDto hubManager = getHubDeliveryManager();

        // 허브 경로 정보 가져오기
        HubPathResDto hubPath = getExactHubPath(request.getSourceHubId(), destinationHubId);

        // 배송 엔티티 생성
        Delivery delivery = Delivery.builder()
                .orderId(request.getOrderId())
                .sourceHubId(request.getSourceHubId())
                .destinationHubId(destinationHubId)
                .address(request.getAddress())
                .recipientName(request.getRecipientName())
                .slackId(request.getSlackId())
                .status(Status.PENDING)
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


    @Transactional
    public DeliveryUpdateResDto updateDeliveryStatus(UUID deliveryId, DeliveryUpdateReqDto request) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 배송이 존재하지 않습니다."));

        delivery.updateStatus(request.getStatus());

        DeliveryPath deliveryPath = delivery.getDeliveryPath();

        handleDeliveryManagerStatus(request.getStatus(), deliveryPath, delivery.getDestinationHubId());

        // 배송 상태 값에 따라 응답 배송 담당자 지정 (Ex.허브이동중: 허브 배송 담당자)
        UUID currentDeliveryManagerId = getCurrentDeliveryManagerId(delivery);

        return DeliveryUpdateResDto.builder()
                .status(delivery.getStatus())
                .currentDeliveryManagerId(currentDeliveryManagerId)
                .build();
    }

    private void handleDeliveryManagerStatus(Status status, DeliveryPath deliveryPath, UUID destinationHubId) {
        UUID deliveryManagerId;
        String deliveryManagerStatus;

        switch (status) {
            case MOVING_TO_HUB:
                deliveryManagerId = deliveryPath.getHubDeliveryManagerId();
                deliveryManagerStatus = "배송중";
                break;

            case DELIVERED_TO_HUB:
                deliveryManagerId = deliveryPath.getHubDeliveryManagerId();
                deliveryManagerStatus = "배송대기";

                // 허브로 배송 완료 시점에 업체 배송 담당자 정보 저장
                DeliveryManagerResDto vendorManager = getVendorDeliveryManager(destinationHubId);
                deliveryPath.updateVendorDeliveryManager(vendorManager.getDeliveryManagerId(), vendorManager.getSequence());
                break;

            case MOVING_TO_VENDOR:
                deliveryManagerId = deliveryPath.getVendorDeliveryManagerId();
                deliveryManagerStatus = "배송중";
                break;

            case DELIVERED_TO_VENDOR:
                deliveryManagerId = deliveryPath.getVendorDeliveryManagerId();
                deliveryManagerStatus = "배송대기";
                break;

            default:
                throw new IllegalArgumentException("처리할 수 없는 상태입니다: " + status);
        }

        // FeignClient 호출
        userClient.updateDeliveryManagerStatus(deliveryManagerId, deliveryManagerStatus);
    }


    private UUID getCurrentDeliveryManagerId(Delivery delivery) {
        // 상태에 따라 허브 담당자 또는 업체 담당자 반환
        if (delivery.getStatus() == Status.PENDING || delivery.getStatus() == Status.MOVING_TO_HUB) {
            return delivery.getDeliveryPath().getHubDeliveryManagerId(); // 허브 담당자
        } else {
            return delivery.getDeliveryPath().getVendorDeliveryManagerId(); // 업체 담당자
        }
    }
}