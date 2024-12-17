package com.logistics.hub.infrastructure.filter;

import static com.logistics.hub.application.service.CustomPrincipal.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.logistics.hub.application.service.CustomPrincipal;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CustomAuthorizationFilter")
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		log.info("path : {}", request.getRequestURI());

		if(request.getRequestURI().contains("/swagger-ui")
				|| request.getRequestURI().contains("/v3/api-docs")
				|| request.getRequestURI().contains("/swagger-ui.html")
				|| request.getRequestURI().contains("/webjars/swagger-ui")
		){

			log.info("SWAGGER 들어왔다!!!!!!!");

			log.info("FilterChain 실행 시작");
			filterChain.doFilter(request, response);
			log.info("FilterChain 실행 완료");
			return;
		}

		// 헤더에서 사용자 정보 추출
		String userId = request.getHeader("X-User-Id");
		String role = request.getHeader("X-Role");

		// 사용자 정보가 없으면 인증 실패 처리
		if (userId == null || role == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		CustomPrincipal principal = createPrinciple(userId, role);

		// 인증 정보 설정
		List<SimpleGrantedAuthority> authorities = Arrays.stream(role.split(","))
			.map(SimpleGrantedAuthority::new)
			.toList();

		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(principal, null, authorities)
		);

		filterChain.doFilter(request, response);
	}
}
