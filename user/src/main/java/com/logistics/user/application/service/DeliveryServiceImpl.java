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
    public DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request, String username, String role) {

        User user = userRepository.findByIdAndRoleAndIsDeleteFalse(request.getUserId(), UserRole.NORMAL)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        // role이 허브 매니저일때 & 배송담당자가 허브 배송담당자가 아닐때
        if(role.equals(UserRole.HUB_MANAGER.toString()) && request.getSourceHubId() != null && request.getDeliveryManagerType() == DeliveryManagerType.VENDOR_DELIVERY){

            // Hub Manager 찾는다.
            User HubManageruser = userRepository.findByUsernameAndIsDeleteFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("HubManager is Not Found"));

            try{
                // request의 HubId와 허브 매니저의 hubid가 같지 않으면 error
                if(!hubFeignService.getUserHubId(HubManageruser.getId()).equals(request.getSourceHubId())){
                    throw new IllegalArgumentException("다른 허브의 배송 담당자 입니다.");
                }
            }catch(Exception e){
                // FeignError
                throw new IllegalArgumentException("허브 매니저가 없습니다.");
            }

        }

        user.modifyRole(UserRole.DELIVERY_MANAGER);


        DeliveryManager manager = deliveryManagerRepository.save(DeliveryManager.createDeliveryManager(user,
                request.getDeliveryManagerType(), request.getSourceHubId()));

        return DeliveryManagerSearchResDto.toResponse(manager);
    }

    @Transactional
    @Override
    public void deleteDeliveryManager(UUID deliveryManagerId, String username, String role) {

        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeleteFalse(deliveryManagerId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryManager Not Found"));

        // role이 허브 매니저일때 & 배송담당자가 허브 배송담당자가 아닐때
        if(role.equals(UserRole.HUB_MANAGER.toString()) && deliveryManager.getSourceHubId() != null){

            User user = userRepository.findByUsernameAndIsDeleteFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("HubManager is Not Found"));

            if(!hubFeignService.getUserHubId(user.getId()).equals(deliveryManager.getSourceHubId())){
                throw new IllegalArgumentException("다른 허브의 배송 담당자 입니다.");
            }

        }

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
