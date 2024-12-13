package com.logistics.user.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    private final SecretKey secretKey;

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${service.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final String BEARER_PREFIX = "Bearer ";

    public TokenService(@Value("${service.jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    // AccessToken 생성
    public String createAccessToken(String username, String role) {
        return BEARER_PREFIX + Jwts.builder()
                .claim("X-User-Id", username)
                .claim("X-Role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // RefreshToken 생성 (여기에 Cacheable 넣는다.)
    public String createRefreshToken(Long userId, String username, String role) {
        return BEARER_PREFIX + Jwts.builder()
                .subject("RefreshToken")
                .claim("X-User-Id", username)
                .claim("X-Role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    private String extractToken(String refreshToken) {
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            return refreshToken.substring(7);
        }
        return null;
    }

    private boolean validateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build().parseSignedClaims(refreshToken);

            if(claimsJws.getPayload().getSubject() == null) {

                System.out.println("###### : claimsJws.getPayload().getSubject() : " + claimsJws.getPayload().getSubject());

                return false;
            }

            String username = claimsJws.getPayload().get("X-User-Id").toString();
            String role = claimsJws.getPayload().get("X-Role").toString();

            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
