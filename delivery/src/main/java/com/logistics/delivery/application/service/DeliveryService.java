package com.logistics.delivery.application.service;

import com.logistics.delivery.application.dto.*;
import com.logistics.delivery.domain.entity.Delivery;
import com.logistics.delivery.domain.entity.DeliveryPath;
import com.logistics.delivery.domain.entity.Status;
import com.logistics.delivery.domain.repository.DeliveryPathRepository;
import com.logistics.delivery.domain.repository.DeliveryRepository;
import com.logistics.delivery.infrastructure.client.HubClient;
import com.logistics.delivery.infrastructure.client.UserClient;
import com.logistics.delivery.infrastructure.client.VendorClient;
import com.logistics.delivery.infrastructure.client.dto.DeliveryManagerResDto;
import com.logistics.delivery.infrastructure.client.dto.HubPathResDto;
import com.logistics.delivery.infrastructure.client.dto.HubResDto;
import com.logistics.delivery.infrastructure.client.dto.VendorResDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryPathRepository deliveryPathRepository;
    private final HubClient hubClient;
    private final UserClient userClient;
    private final VendorClient vendorClient;

    // 허브 경로 정보 가져오기
    public HubPathResDto getExactHubPath(UUID sourceHubId, UUID destinationHubId) {
        return hubClient.getExactHubPath(sourceHubId, destinationHubId);
    }

    // 허브 id로 허브 주소 가져오기
    public HubResDto getHub(UUID hubId) {
        return hubClient.getHub(hubId);
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
    public DeliveryCreateResDto createDelivery(DeliveryCreateReqDto request) {
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

        return DeliveryCreateResDto.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .deliveryPathId(deliveryPath.getId())
                .sourceHubAddress(getHub(request.getSourceHubId()).getAddress())
                .hubDeliveryManagerId(hubManager.getDeliveryManagerId())
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
                deliveryManagerStatus = "IN_DELIVERY"; // 배송중
                break;

            case DELIVERED_TO_HUB:
                deliveryManagerId = deliveryPath.getHubDeliveryManagerId();
                deliveryManagerStatus = "PENDING_DELIVERY"; // 배송 대기

                // 허브로 배송 완료 시점에 업체 배송 담당자 정보 저장
                DeliveryManagerResDto vendorManager = getVendorDeliveryManager(destinationHubId);
                deliveryPath.updateVendorDeliveryManager(vendorManager.getDeliveryManagerId(), vendorManager.getSequence());
                break;

            case MOVING_TO_VENDOR:
                deliveryManagerId = deliveryPath.getVendorDeliveryManagerId();
                deliveryManagerStatus = "IN_DELIVERY";
                break;

            case DELIVERED_TO_VENDOR:
                deliveryManagerId = deliveryPath.getVendorDeliveryManagerId();
                deliveryManagerStatus = "PENDING_DELIVERY";
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

    @Transactional
    public void deleteDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 배송을 찾을 수 없습니다: " + deliveryId));

        if (delivery.isDelete()) {
            throw new IllegalStateException("이미 삭제된 배송입니다..");
        }
        DeliveryPath deliveryPath = delivery.getDeliveryPath();

        deliveryPath.setIsDelete();
        delivery.setIsDelete();
    }

    @Transactional
    public DeliveryResDto getDeliveryById(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findByIdAndIsDeleteFalse(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송 정보를 찾을 수 없습니다: " + deliveryId));

        return DeliveryResDto.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
                .sourceHubId(delivery.getSourceHubId())
                .destinationHubId(delivery.getDestinationHubId())
                .address(delivery.getAddress())
                .recipientName(delivery.getRecipientName())
                .slackId(delivery.getSlackId())
                .status(delivery.getStatus())
                .createdAt(delivery.getCreatedAt())
                .createdBy(delivery.getCreatedBy())
                .updatedAt(delivery.getUpdatedAt())
                .updatedBy(delivery.getUpdatedBy())
                .build();
    }

    public Page<DeliveryResDto> getDeliveries(String condition, String keyword,
                                              String status, int pageNumber, boolean isAsc) {
        Sort sort = isAsc ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(pageNumber, 10, sort);

        Page<Delivery> deliveryPage = deliveryRepository.getDeliveries(condition, keyword, status, pageable);

        return deliveryPage.map(delivery -> DeliveryResDto.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
                .sourceHubId(delivery.getSourceHubId())
                .destinationHubId(delivery.getDestinationHubId())
                .address(delivery.getAddress())
                .recipientName(delivery.getRecipientName())
                .slackId(delivery.getSlackId())
                .createdAt(delivery.getCreatedAt())
                .createdBy(delivery.getCreatedBy())
                .updatedAt(delivery.getUpdatedAt())
                .updatedBy(delivery.getUpdatedBy())
                .build());
    }

    @Transactional
    public DeliveryPathResDto getDeliveryPathById(UUID deliveryPathId) {
        DeliveryPath deliveryPath = deliveryPathRepository.findByIdAndIsDeleteFalse(deliveryPathId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송 경로를 찾을 수 없습니다: " + deliveryPathId));

        return DeliveryPathResDto.builder()
                .id(deliveryPath.getId())
                .hubDeliveryManagerId(deliveryPath.getHubDeliveryManagerId())
                .hubDeliveryManagerSequence(deliveryPath.getHubDeliveryManagerSequence())
                .vendorDeliveryManagerId(deliveryPath.getVendorDeliveryManagerId())
                .vendorDeliveryManagerSequence(deliveryPath.getVendorDeliveryManagerSequence())
                .sourceHubId(deliveryPath.getSourceHubId())
                .destinationHubId(deliveryPath.getDestinationHubId())
                .distance(deliveryPath.getDistance())
                .estimatedTime(deliveryPath.getEstimatedTime())
                .status(deliveryPath.getStatus())
                .createdAt(deliveryPath.getCreatedAt())
                .createdBy(deliveryPath.getCreatedBy())
                .updatedAt(deliveryPath.getUpdatedAt())
                .updatedBy(deliveryPath.getUpdatedBy())
                .build();
    }

}
