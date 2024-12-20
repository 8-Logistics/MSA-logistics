package com.logistics.user.application.service;

import com.logistics.user.application.HubFeignService;
import com.logistics.user.application.dto.*;
import com.logistics.user.domain.entity.DeliveryManager;
import com.logistics.user.domain.entity.User;
import com.logistics.user.domain.enums.DeliveryManagerType;
import com.logistics.user.domain.enums.DeliveryStatus;
import com.logistics.user.domain.enums.UserRole;
import com.logistics.user.domain.repository.DeliveryManagerRepository;
import com.logistics.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryManagerService{

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserRepository userRepository;
    private final HubFeignService hubFeignService;

    @Transactional
    @Override
    public DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request, String username, String role) {

        User user = userRepository.findByIdAndRoleAndIsDeleteFalse(request.getUserId(), UserRole.NORMAL)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        if(request.getDeliveryManagerType() == DeliveryManagerType.HUB_DELIVERY){

            if(request.getSourceHubId() != null){
                throw new IllegalArgumentException("허브 배송 담당자는 HubId를 parameter 에서 빼고 요청해주세요.");
            }

            if(role.equals(UserRole.HUB_MANAGER.toString())){
                throw new IllegalArgumentException("허브 배송 담당자는 HUB_MANAGER가 바꾸지 못합니다.");
            }


        }else{

            if(request.getSourceHubId() == null){
                throw new IllegalArgumentException("업체 배송 담당자는 HubId를 parameter에 추가해야 합니다.");
            }

            if(role.equals(UserRole.HUB_MANAGER.toString())){
                // Hub Manager 찾는다.
                User hubManageruser = userRepository.findByUsernameAndIsDeleteFalse(username)
                        .orElseThrow(() -> new IllegalArgumentException("HubManager is Not Found"));

                try{
                    // request의 HubId와 허브 매니저의 hubid가 같지 않으면 error
                    if(!hubFeignService.getUserHubId(hubManageruser.getId()).equals(request.getSourceHubId())){
                        throw new IllegalArgumentException("다른 허브의 배송 담당자 입니다.");
                    }
                }catch(Exception e){
                    // FeignError
                    throw new IllegalArgumentException("허브 매니저가 없습니다.");
                }
            }

            // 허브 id 있는지 체크
            if(!hubFeignService.checkHub(request.getSourceHubId())){
                throw new IllegalArgumentException("Hub Not Found");
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
        if(role.equals(UserRole.HUB_MANAGER.toString())){

            if(deliveryManager.getSourceHubId() == null){
                throw new IllegalArgumentException("sourceHubId를 추가해주세요");
            }

            User user = userRepository.findByUsernameAndIsDeleteFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("HubManager is Not Found"));

            try{
                if(!hubFeignService.getUserHubId(user.getId()).equals(deliveryManager.getSourceHubId())){
                    throw new IllegalArgumentException("다른 허브의 배송 담당자 입니다.");
                }
            }catch(Exception e){
                // Feign Error
                throw new IllegalArgumentException("Hub Feign Error");
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

        if(deliveryManager.getDeliveryStatus().getDescription().equals(deliveryStatus)){
            throw new IllegalArgumentException("전 상태와 같은 배송 status 입니다.");
        }

        if(DeliveryStatus.IN_DELIVERY.toString().equals(deliveryStatus)){
            deliveryManager.updateDeliverySequence();
        }

        deliveryManager.updateDeliveryStatus(deliveryStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public DeliverySequenceDto getDeliverySequence(UUID hubId) {

        // 허브 배송담당자
        if(hubId == null){
            DeliveryManager deliveryManager = deliveryManagerRepository
                    .findTopBySourceHubIdIsNullAndIsDeleteFalseOrderByDeliverySequenceAscCreatedAtAsc()
                    .orElseThrow(() -> new IllegalArgumentException("배송이 가능한 허브 배송 담당자가 없습니다."));

            return DeliverySequenceDto.toResponse(deliveryManager.getId(), deliveryManager.getDeliverySequence());

        }

        // 업체 배송 담당자
        DeliveryManager deliveryManager = deliveryManagerRepository
                .findTopBySourceHubIdAndIsDeleteFalseOrderByDeliverySequenceAscCreatedAtAsc(hubId)
                .orElseThrow(() -> new IllegalArgumentException("배송이 가능한 업체 배송 담당자가 없습니다."));

        return DeliverySequenceDto.toResponse(deliveryManager.getId(), deliveryManager.getDeliverySequence());
    }

    @Transactional(readOnly = true)
    @Override
    public DeliveryManagerSearchResDto getDeliveryManager(UUID deliveryManagerId) {

        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeleteFalse(deliveryManagerId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryManager Not Found"));

        return DeliveryManagerSearchResDto.toResponse(deliveryManager);
    }

    @Transactional
    @Override
    public DeliveryManagerSearchResDto modifyDeliveryManager(UUID deliveryManagerId, DeliveryManagerUpdateReqDto request
            , String username, String role) {

        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeleteFalse(deliveryManagerId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryManager Not Found"));

        if(deliveryManager.getDeliveryStatus() == DeliveryStatus.IN_DELIVERY){
            throw new IllegalArgumentException("배송중인 배송담당자 입니다. 추후에 다시 요청하세요");
        }

        if(deliveryManager.getDeliveryManagerType() == request.getDeliveryManagerType()){
            throw new IllegalArgumentException("이미 같은 배송 타입의 배송담당자입니다.");
        }

        // 현재 업체 배송 담당자 -> 허브 배송 담당자
        if(deliveryManager.getSourceHubId() != null){

            if(request.getSourceHubId() != null){
                throw new IllegalArgumentException("이미 같은 배송 타입의 배송담당자입니다.");
            }
        // 현재 허브 배송 담당자 -> 업체 배송 담당자
        }else{
            if(request.getSourceHubId() == null){
                throw new IllegalArgumentException("이미 같은 배송 타입의 배송담당자입니다.");
            }

            // 허브 매니저일때
            if(role.equals(UserRole.HUB_MANAGER.getAuthority())) {
                User HubManageruser = userRepository.findByUsernameAndIsDeleteFalse(username)
                        .orElseThrow(() -> new IllegalArgumentException("HubManager is Not Found"));


                try{
                    // request의 HubId와 허브 매니저의 hubid가 같지 않으면 error
                    if(!hubFeignService.getUserHubId(HubManageruser.getId()).equals(request.getSourceHubId())){
                        throw new IllegalArgumentException("다른 허브의 업체 배송 담당자입니다.");
                    }
                }catch(Exception e){
                    // FeignError
                    throw new IllegalArgumentException("Hub Feign Error.");
                }
            // Master일때
            }else{

                try{
                    if(!hubFeignService.checkHub(request.getSourceHubId())){
                        throw new IllegalArgumentException("Hub Not Found");
                    }
                }catch(Exception e){
                    // FeignError
                    throw new IllegalArgumentException("Hub Feign Error");
                }
            }

        }

        deliveryManager.deliveryManagerTypeAndHubId(request.getDeliveryManagerType(), request.getSourceHubId());
        // sequence reset
        deliveryManager.resetDeliverySequence();
        deliveryManager.updateDeliveryStatus(DeliveryStatus.PENDING_DELIVERY.getDescription());

        return DeliveryManagerSearchResDto.toResponse(deliveryManager);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DeliveryManagerSearchResDto> getDeliveryManagerSearch(
            DeliveryManagerSearchReqDto request, Pageable pageable, String username, String role) {

        if(role.equals(UserRole.HUB_MANAGER.getAuthority())){

            if(request.getHubIdList().isEmpty()){
                throw new IllegalArgumentException("HUB_MANAGER 권한일때 HubIdList 추가해주세요");
            }

            User hubManageruser = userRepository.findByUsernameAndIsDeleteFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("HubManager is Not Found"));

            try{
                // request의 HubId와 허브 매니저의 hubid가 같지 않으면 error
                if(!hubFeignService.getUserHubId(hubManageruser.getId()).equals(request.getHubIdList().get(0))){
                    throw new IllegalArgumentException("자기 자신의 허브만 검색할 수 있습니다.");
                }
            }catch(Exception e){
                // FeignError
                throw new IllegalArgumentException("[Feign] Hub Feign Error");
            }

        }

        return deliveryManagerRepository.getDeliveryManagerSearch(request, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public String getDeliveryManagerUserId(UUID deliveryManagerId) {

        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeleteFalse(deliveryManagerId)
                .orElseThrow(() -> new IllegalArgumentException("DeliveryManager Not Found"));

        return deliveryManager.getUser().getUsername();
    }
}
