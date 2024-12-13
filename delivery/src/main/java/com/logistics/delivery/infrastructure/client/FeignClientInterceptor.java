package com.logistics.delivery.infrastructure.client;

import com.logistics.delivery.application.CustomPrincipal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Java 16부터 도입된 pattern matching 기능이라고 한다. 변수가 자동으로 캐스팅되어 저장된다고 함.
        if (authentication != null && authentication.getPrincipal() instanceof CustomPrincipal) {
            CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
            String username = principal.getUserId();
            String role = principal.getRole();
            requestTemplate.header("X-User-Id", username);
            requestTemplate.header("X-Role", role);

        }
    }
}


