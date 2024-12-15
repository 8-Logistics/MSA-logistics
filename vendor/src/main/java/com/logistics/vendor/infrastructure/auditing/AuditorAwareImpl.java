package com.logistics.vendor.infrastructure.auditing;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.logistics.vendor.application.service.CustomPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

	private final HttpServletRequest request;

	@Override
	public Optional<String> getCurrentAuditor() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null
			|| !authentication.isAuthenticated()
			|| !(authentication.getPrincipal() instanceof CustomPrincipal)) {

			return Optional.empty();
		}

		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();

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
