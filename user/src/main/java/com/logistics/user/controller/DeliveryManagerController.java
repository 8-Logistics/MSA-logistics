package com.logistics.user.controller;

import com.logistics.user.application.CustomPrincipal;
import com.logistics.user.application.dto.*;
import com.logistics.user.application.service.DeliveryManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    // 배송 담당자 생성 API
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @PostMapping("/delivery-manager")
    public ResponseEntity<?> approveDeliveryManager(@RequestBody DeliveryManagerCreateReqDto request, @AuthenticationPrincipal CustomPrincipal principal){
        DeliveryManagerSearchResDto response = deliveryManagerService.approveDeliveryManager(request, principal.getUserId(), principal.getRole());
        return ResponseEntity.ok(ApiResponse.success("배송담당자 생성 success", response));
    }

    // 배송 담당자 삭제 API
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @DeleteMapping("/delivery-manager/{deliveryManagerId}")
    public ResponseEntity<?> deleteDeliveryManager(@PathVariable UUID deliveryManagerId, @AuthenticationPrincipal CustomPrincipal principal){
        deliveryManagerService.deleteDeliveryManager(deliveryManagerId, principal.getUserId(), principal.getRole());
        return ResponseEntity.ok(ApiResponse.success("배송담당자가 삭제되었습니다."));
    }

    // [Feign] 배송 상태 update API
    @PreAuthorize("hasAnyAuthority('MASTER','DELIVERY_MANAGER')")
    @PutMapping("/delivery-manager/{deliveryManagerId}/updateStatus")
    public void updateDeliveryStatus(@PathVariable UUID deliveryManagerId, @RequestParam("deliveryStatus")
                                                  String deliveryStatus){
        deliveryManagerService.updateDeliveryStatus(deliveryManagerId, deliveryStatus);
    }

    // [Feign] 배송 sequence return API
    @GetMapping("/delivery-manager/deliverySequence")
    public DeliverySequenceDto getDeliverySequence(@RequestParam(value = "hubId", required = false) UUID hubId){
        return deliveryManagerService.getDeliverySequence(hubId);
    }

    // [Feign] order 배송담당자 UUID로 userId return API
    @GetMapping("/delivery-manager/{deliveryManagerId}/order")
    public ResponseEntity<String> getOrderUserId(@PathVariable UUID deliveryManagerId){
        return ResponseEntity.ok(deliveryManagerService.getDeliveryManagerUserId(deliveryManagerId));
    }

    // 배송 담당자 단건 조회 API
    @PreAuthorize("hasAnyAuthority('MASTER','DELIVERY_MANAGER', 'HUB_MANAGER')")
    @GetMapping("/delivery-manager/{deliveryManagerId}")
    public ResponseEntity<?> getDeliveryManager(@PathVariable UUID deliveryManagerId){

        DeliveryManagerSearchResDto response = deliveryManagerService.getDeliveryManager(deliveryManagerId);

        return ResponseEntity.ok(ApiResponse.success("배송 담당자 단건 조회 success", response));
    }

    // 배송담당자 수정 API
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @PutMapping("/delivery-manager/{deliveryManagerId}")
    public ResponseEntity<?> modifyDeliveryManager(@PathVariable UUID deliveryManagerId, @RequestBody DeliveryManagerUpdateReqDto request,
                @AuthenticationPrincipal CustomPrincipal principal){

        DeliveryManagerSearchResDto response = deliveryManagerService.modifyDeliveryManager(deliveryManagerId, request,
                principal.getUserId(), principal.getRole());

        return ResponseEntity.ok(ApiResponse.success("배송 담당자 수정 success", response));
    }

    // 배송 담당자 조회 API
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @GetMapping("/delivery-manager/search")
    public ResponseEntity<?>
                    getDeliveryManagerSearch(DeliveryManagerSearchReqDto request,
                    Pageable pageable,
                    @AuthenticationPrincipal CustomPrincipal principal
        ){

        Page<DeliveryManagerSearchResDto> response = deliveryManagerService
                                        .getDeliveryManagerSearch(request, pageable, principal.getUserId(), principal.getRole());

        return ResponseEntity.ok(ApiResponse.success("배송 담당자 조회 success", response));
    }
}
