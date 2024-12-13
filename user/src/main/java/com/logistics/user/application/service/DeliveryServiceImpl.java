package com.logistics.user.application.service;

import com.logistics.user.application.HubFeignService;
import com.logistics.user.application.dto.DeliveryManagerCreateReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;
import com.logistics.user.domain.entity.DeliveryManager;
import com.logistics.user.domain.entity.User;
import com.logistics.user.domain.enums.DeliveryManagerType;
import com.logistics.user.domain.enums.DeliveryStatus;
import com.logistics.user.domain.enums.UserRole;
import com.logistics.user.domain.repository.DeliveryManagerRepository;
import com.logistics.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryManagerService{

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserRepository userRepository;
    private HubFeignService hubFeignService;

    @Transactional
    @Override
    public DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request) {

        User user = userRepository.findByIdAndRoleAndIsDeleteFalse(request.getUserId(), UserRole.NORMAL)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        user.modifyRole(UserRole.DELIVERY_MANAGER);

        if(request.getSourceHubId() != null && request.getDeliveryManagerType() == DeliveryManagerType.VENDOR_DELIVERY){

            if(!hubFeignService.checkHub(request.getSourceHubId())){
                throw new IllegalArgumentException("Hub Not Found");
            }
        }

        DeliveryManager manager = deliveryManagerRepository.save(DeliveryManager.createDeliveryManager(user,
                request.getDeliveryManagerType(), request.getSourceHubId()));

        return DeliveryManagerSearchResDto.toResponse(manager);
    }

    @Transactional
    @Override
    public void deleteDeliveryManager(UUID deliveryManagerId) {

        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeleteFalse(deliveryManagerId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryManager Not Found"));

        if(deliveryManager.getDeliveryStatus() == DeliveryStatus.IN_DELIVERY){
            throw new IllegalArgumentException("배송중인 배송담당자 입니다. 추후에 다시 요청하세요");
        }
        deliveryManager.setIsDelete();

    }

    @Transactional
    @Override
    public void updateDeliveryStatus(UUID deliveryManagerId, String deliveryStatus) {

        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeleteFalse(deliveryManagerId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryManager Not Found"));

        if(DeliveryStatus.IN_DELIVERY.toString().equals(deliveryStatus)){
            deliveryManager.updateDeliverySequence();
        }

        deliveryManager.updateDeliveryStatus(deliveryStatus);
    }
}
