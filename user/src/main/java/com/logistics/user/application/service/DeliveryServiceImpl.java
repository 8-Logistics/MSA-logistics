package com.logistics.user.application.service;

import com.logistics.user.application.HubService;
import com.logistics.user.application.dto.DeliveryManagerCreateReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;
import com.logistics.user.domain.entity.DeliveryManager;
import com.logistics.user.domain.entity.User;
import com.logistics.user.domain.enums.DeliveryManagerType;
import com.logistics.user.domain.enums.UserRole;
import com.logistics.user.domain.repository.DeliveryManagerRepository;
import com.logistics.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryManagerService{

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserRepository userRepository;
    private HubService hubService;

    @Override
    public DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request) {

        User user = userRepository.findByIdAndRoleAndIsDeleteFalse(request.getUserId(), UserRole.NORMAL)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        user.modifyRole(UserRole.DELIVERY_MANAGER);

        if(request.getSourceHubId() != null && request.getDeliveryManagerType() == DeliveryManagerType.VENDOR_DELIVERY){

            if(!hubService.checkHub(request.getSourceHubId())){
                throw new IllegalArgumentException("Hub Not Found");
            }
        }

        DeliveryManager manager = deliveryManagerRepository.save(DeliveryManager.createDeliveryManager(user,
                request.getDeliveryManagerType(), request.getSourceHubId()));

        return DeliveryManagerSearchResDto.toResponse(manager);
    }
}
