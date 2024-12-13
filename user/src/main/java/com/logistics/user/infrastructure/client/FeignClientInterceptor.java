package com.logistics.user.infrastructure.client;

import com.logistics.user.application.CustomPrincipal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Java 16부터 도입된 pattern matching 기능이라고 한다. 변수가 자동으로 캐스팅되어 저장된다고 함.
        if (authentication != null && authentication.getPrincipal() instanceof CustomPrincipal customPrincipal) {

            String username = customPrincipal.getUserId();
            String role = customPrincipal.getRole();

            requestTemplate.header("X-User-Id", username);
            requestTemplate.header("X-Role", role);
        }
    }
}


