package com.logistics.hub.infrastructure.auditing;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

	// private final HttpServletRequest request;
	//
	// @Value("${JWT_SECRET_KEY}")
	// private String jwtSecretKey;
	//
	// public AuditorAwareImpl(HttpServletRequest request) {
	// 	this.request = request;
	// }
	//
	@Override
	public Optional<String> getCurrentAuditor() {
		// 	// Authorization 헤더에서 JWT 토큰 추출
		// 	String token = request.getHeader("Authorization");
		// 	if (token == null || !token.startsWith("Bearer ")) {
		// 		return Optional.empty();
		// 	}
		//
		// 	// "Bearer " 부분 제거
		// 	token = token.substring(7);
		//
		// 	// JWT 토큰 파싱
		// 	try {
		// 		Claims claims = Jwts.parser()
		// 			.setSigningKey(jwtSecretKey)
		// 			.build()
		// 			.parseClaimsJws(token)
		// 			.getBody();
		//
		// 		// payload의 사용자 ID 추출
		// 		String userId = claims.get("X-User-Id", String.class);
		// 		return Optional.ofNullable(userId).or(() -> Optional.of("anonymous"));
		// 	} catch (Exception e) {
		// 		return Optional.empty();
		// 	}
		return Optional.of("mock user");
	}

}
