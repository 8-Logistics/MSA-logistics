package com.logistics.vendor.infrastructure.config;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CustomAccessDeniedHandler")
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final String ROLE_PREFIX = "ROLE_";

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException ex)
		throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authorities = "";
		if (auth != null) {
			authorities = auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(", "))
				.replace(ROLE_PREFIX, "");
		}

		// TODO 추후에 바꿀 것.

		String commonResponse =
			"권한이 없습니다. 현재 사용자 권한: " + authorities;

		new ObjectMapper().writeValue(response.getOutputStream(), commonResponse);
	}
}
