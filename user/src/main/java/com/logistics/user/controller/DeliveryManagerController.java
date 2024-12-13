package com.logistics.user.controller;

import com.logistics.user.application.dto.DeliveryManagerCreateReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;
import com.logistics.user.application.service.DeliveryManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PreAuthorize("hasAnyAuthority('MASTER')")
    @PostMapping("/delivery-manager")
    public ResponseEntity<?> approveDeliveryManager(@RequestBody DeliveryManagerCreateReqDto request){
        DeliveryManagerSearchResDto response = deliveryManagerService.approveDeliveryManager(request);
        return ResponseEntity.ok(response);
    }





}
