package com.logistics.user.controller;

import com.logistics.user.application.dto.DeliveryManagerCreateReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;
import com.logistics.user.application.service.DeliveryManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    // 배송 담당자 생성 API
    @PreAuthorize("hasAnyAuthority('MASTER')")
    @PostMapping("/delivery-manager")
    public ResponseEntity<?> approveDeliveryManager(@RequestBody DeliveryManagerCreateReqDto request){
        DeliveryManagerSearchResDto response = deliveryManagerService.approveDeliveryManager(request);
        return ResponseEntity.ok(response);
    }

    // 배송 담당자 삭제 API
    @DeleteMapping("/delivery-manager/{deliveryId}")
    public ResponseEntity<?> deleteDeliveryManager(@PathVariable UUID deliveryId){
        deliveryManagerService.deleteDeliveryManager(deliveryId);
        return ResponseEntity.ok("배송담당자가 삭제되었습니다.");
    }

}
