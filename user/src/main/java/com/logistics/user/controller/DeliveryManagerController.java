package com.logistics.user.controller;

import com.logistics.user.application.CustomPrincipal;
import com.logistics.user.application.dto.*;
import com.logistics.user.application.service.DeliveryManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Delivery-Manager-Service API", description = "배송 담당자 API Controller")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @Operation(summary = "배송담당자 생성 API", description = "MASTER, HUB_MANAGER 권한만 NORMAL 권한인 사용자에서 배송담당자로")
    // 배송 담당자 생성 API
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @PostMapping("/delivery-manager")
    public ResponseEntity<?> approveDeliveryManager(@RequestBody DeliveryManagerCreateReqDto request, @AuthenticationPrincipal CustomPrincipal principal){
        DeliveryManagerSearchResDto response = deliveryManagerService.approveDeliveryManager(request, principal.getUserId(), principal.getRole());
        return ResponseEntity.ok(ApiResponse.success("배송담당자 생성 success", response));
    }

    // 배송 담당자 삭제 API
    @Operation(summary = "배송담당자 삭제 API", description = "MASTER, HUB_MANAGER 권한만 삭제")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @DeleteMapping("/delivery-manager/{deliveryManagerId}")
    public ResponseEntity<?> deleteDeliveryManager(@PathVariable UUID deliveryManagerId, @AuthenticationPrincipal CustomPrincipal principal){
        deliveryManagerService.deleteDeliveryManager(deliveryManagerId, principal.getUserId(), principal.getRole());
        return ResponseEntity.ok(ApiResponse.success("배송담당자가 삭제되었습니다."));
    }

    // [Feign] 배송 상태 update API
    @Operation(summary = "[Feign] 배송 상태 update API", description = "MASTER, DELIVERY_MANAGER 권한만 삭제")
    @PreAuthorize("hasAnyAuthority('MASTER','DELIVERY_MANAGER')")
    @PutMapping("/delivery-manager/{deliveryManagerId}/updateStatus")
    public void updateDeliveryStatus(@PathVariable UUID deliveryManagerId, @RequestParam("deliveryStatus")
                                                  String deliveryStatus){
        deliveryManagerService.updateDeliveryStatus(deliveryManagerId, deliveryStatus);
    }

    // [Feign] 배송 sequence return API
    @Operation(summary = "[Feign] 배송 sequence return API")
    @GetMapping("/delivery-manager/deliverySequence")
    public DeliverySequenceDto getDeliverySequence(@RequestParam(value = "hubId", required = false) UUID hubId){
        return deliveryManagerService.getDeliverySequence(hubId);
    }

    // [Feign] order 배송담당자 UUID로 userId return API
    @Operation(summary = "[Feign] Order 배송담당자 userID return API", description = "order Feign 배송담당자 UUID로 userID return")
    @GetMapping("/delivery-manager/{deliveryManagerId}/order")
    public ResponseEntity<String> getOrderUserId(@PathVariable UUID deliveryManagerId){
        return ResponseEntity.ok(deliveryManagerService.getDeliveryManagerUserId(deliveryManagerId));
    }

    // 배송 담당자 단건 조회 API
    @Operation(summary = "배송담당자 단건 조회 API")
    @PreAuthorize("hasAnyAuthority('MASTER','DELIVERY_MANAGER', 'HUB_MANAGER')")
    @GetMapping("/delivery-manager/{deliveryManagerId}")
    public ResponseEntity<?> getDeliveryManager(@PathVariable UUID deliveryManagerId){

        DeliveryManagerSearchResDto response = deliveryManagerService.getDeliveryManager(deliveryManagerId);

        return ResponseEntity.ok(ApiResponse.success("배송 담당자 단건 조회 success", response));
    }

    // 배송담당자 수정 API
    @Operation(summary = "배송담당자 수정 API", description = "MASTER, HUB_MANAGER(자신의 허브)만 수정 가능 ")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
    @PutMapping("/delivery-manager/{deliveryManagerId}")
    public ResponseEntity<?> modifyDeliveryManager(@PathVariable UUID deliveryManagerId, @RequestBody DeliveryManagerUpdateReqDto request,
                @AuthenticationPrincipal CustomPrincipal principal){

        DeliveryManagerSearchResDto response = deliveryManagerService.modifyDeliveryManager(deliveryManagerId, request,
                principal.getUserId(), principal.getRole());

        return ResponseEntity.ok(ApiResponse.success("배송 담당자 수정 success", response));
    }

    // 배송 담당자 조회 API
    @Operation(summary = "배송담당자 검색/조회 API", description = "MASTER, HUB_MANAGER 권한만 가능")
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
