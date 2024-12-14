package com.logistics.user.controller;

import com.logistics.user.application.CustomPrincipal;
import com.logistics.user.application.dto.DeliveryManagerCreateReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;
import com.logistics.user.application.service.DeliveryManagerService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 삭제 API
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @DeleteMapping("/delivery-manager/{deliveryManagerId}")
    public ResponseEntity<?> deleteDeliveryManager(@PathVariable UUID deliveryManagerId, @AuthenticationPrincipal CustomPrincipal principal){
        deliveryManagerService.deleteDeliveryManager(deliveryManagerId, principal.getUserId(), principal.getRole());
        return ResponseEntity.ok("배송담당자가 삭제되었습니다.");
    }

    // [Feign] 배송 상테 update API
    @PreAuthorize("hasAnyAuthority('MASTER','DELIVERY_MANAGER')")
    @PutMapping("/delivery-manager/{deliveryManagerId}/updateStatus")
    public void updateDeliveryStatus(@PathVariable UUID deliveryManagerId, @RequestParam("deliveryStatus")
                                                  String deliveryStatus){
        deliveryManagerService.updateDeliveryStatus(deliveryManagerId, deliveryStatus);
    }



}
