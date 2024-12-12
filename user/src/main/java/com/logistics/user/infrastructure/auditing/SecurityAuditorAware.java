package com.logistics.user.infrastructure.auditing;

import com.logistics.user.application.CustomPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j(topic = "auditorAware")
@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<String> {

    private final HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {

        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        log.info("RequestURI: {}", request.getRequestURI());

        if (request.getRequestURI().equals("/api/v1/auth/signUp")) {

            // TODO 이런 방식은 괜찮은지?
            String username = (String) request.getAttribute("username");

            return Optional.of(username);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        // 로그인한 사용자의 ID Masking 처리
        return Optional.of(userNameMasking(principal.getUserId()));
    }

    // 그냥 마스킹 한 느낌만..
    private String userNameMasking(String username) {

        StringBuilder sb = new StringBuilder(username);
        int halflength = sb.length() / 2;

        if (username.length() < 6) {
            sb.replace(halflength - 1, halflength + 1, "**");
        } else {
            sb.replace(halflength - 1, halflength + 2, "***");
        }

        return sb.toString();
    }


}
