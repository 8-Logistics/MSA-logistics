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
    private HubFeignService hubFeignService;

    @Transactional
    @Override
    public DeliveryManagerSearchResDto approveDeliveryManager(DeliveryManagerCreateReqDto request, String username, String role) {

        User user = userRepository.findByIdAndRoleAndIsDeleteFalse(request.getUserId(), UserRole.NORMAL)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        // role이 허브 매니저일때 & 업체 배송 담당자
        if(role.equals(UserRole.HUB_MANAGER.toString()) && request.getSourceHubId() != null && request.getDeliveryManagerType() == DeliveryManagerType.VENDOR_DELIVERY){

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

        // 로그인 role이 Master일때 업체 배송 담당자
        if(request.getSourceHubId() != null && request.getDeliveryManagerType() == DeliveryManagerType.VENDOR_DELIVERY){
            try{
                if(!hubFeignService.checkHub(request.getSourceHubId())){
                    throw new IllegalArgumentException("Hub Not Found");
                }
            }catch(Exception e){
                // FeignError
                throw new IllegalArgumentException("Hub Feign Error");
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

        if(deliveryManager.getDeliveryStatus().getDescription() == deliveryStatus){
            throw new IllegalArgumentException("전 상태와 같은 배송 status 입니다.");
        }

        if(DeliveryStatus.IN_DELIVERY.toString().equals(deliveryStatus)){
            deliveryManager.updateDeliverySequence();
        }

        deliveryManager.updateDeliveryStatus(deliveryStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public DeliverySequenceDto getDeliverySequence(UUID hubId, long deliverySequence) {

        // 허브 배송담당자
        if(hubId == null){
            DeliveryManager deliveryManager = deliveryManagerRepository
                    .findTopBySourceHubIdIsNullAndIsDeleteFalseAndDeliverySequenceGreaterThanOrderByDeliverySequenceAscCreatedAtAsc(deliverySequence)
                    .orElseThrow(() -> new IllegalArgumentException("배송이 가능한 허브 배송 담당자가 없습니다."));

            return DeliverySequenceDto.toResponse(deliveryManager.getId(), deliveryManager.getDeliverySequence());

        }

        // 업체 배송 담당자
        DeliveryManager deliveryManager = deliveryManagerRepository
                .findTopBySourceHubIdAndIsDeleteFalseAndDeliverySequenceGreaterThanOrderByDeliverySequenceAscCreatedAtAsc(hubId, deliverySequence)
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

        // 허브 매니저가 허브 담당 배송담당자에서 -> 업체 배송담당자로 변경
        if(role.equals(UserRole.HUB_MANAGER.getAuthority()) && request.getSourceHubId() != null){

            if(deliveryManager.getSourceHubId() != null){
                throw new IllegalArgumentException("이미 같은 배송 타입의 배송담당자입니다.");
            }

            User HubManageruser = userRepository.findByUsernameAndIsDeleteFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("HubManager is Not Found"));


            try{
                // request의 HubId와 허브 매니저의 hubid가 같지 않으면 error
                if(!hubFeignService.getUserHubId(HubManageruser.getId()).equals(request.getSourceHubId())){
                    throw new IllegalArgumentException("다른 허브의 업체 배송 담당자 수정 요청입니다.");
                }
            }catch(Exception e){
                // FeignError
                throw new IllegalArgumentException("Hub Feign Error.");
            }

        }

        // Master가 허브 담당 배송담당자에서 -> 업체 배송담당자로 변경
        if(role.equals(UserRole.MASTER.getAuthority()) && request.getSourceHubId() != null){

            if(deliveryManager.getSourceHubId() != null){
                throw new IllegalArgumentException("이미 같은 업체 배송 타입의 배송담당자입니다.");
            }

            try{
                if(!hubFeignService.checkHub(request.getSourceHubId())){
                    throw new IllegalArgumentException("Hub Not Found");
                }
            }catch(Exception e){
                // FeignError
                throw new IllegalArgumentException("Hub Feign Error");
            }


        }

        // MASTER가 업체 담당 -> 허브 배송담당자로 변경
        if(role.equals(UserRole.MASTER.getAuthority()) && request.getSourceHubId() == null) {

            if(deliveryManager.getSourceHubId() == null){
                throw new IllegalArgumentException("이미 같은 허브 배송 타입의 배송담당자입니다.");
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

        if(role.equals(UserRole.HUB_MANAGER.getAuthority()) && !request.getHubIdList().isEmpty()){

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
