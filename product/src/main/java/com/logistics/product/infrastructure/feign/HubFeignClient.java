package com.logistics.product.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubFeignClient {
    // Todo feign 완성 시 테스트 필요
    /* 상품 등록 시 소속시킬 허브가 존재하는지 확인
    * hubId가 있는지 없는지 체크 = return true/false */
    @GetMapping("/api/v1/hubs/{hubId}/exists")
    public boolean checkHub(@PathVariable("hubId") UUID hubId);

    /* 상품 수정 시 허브 매니저인 경우 소속허브 ID를 가져와
    상품이 속해있는 소속허브 ID와 같은지 체크하기 위함 = return userId가 속한 hubId*/
    @GetMapping("/api/v1/hubs/user/{userId}")
    public UUID getUserHubId(String userId);
}
