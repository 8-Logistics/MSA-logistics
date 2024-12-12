package com.logistics.user.application.service;

import com.logistics.user.application.dto.UserSignInReqDto;
import com.logistics.user.application.dto.UserSignUpReqDto;
import com.logistics.user.domain.entity.User;
import com.logistics.user.domain.enums.UserRole;
import com.logistics.user.domain.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${service.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final String BEARER = "Bearer ";


    public AuthServiceImpl(UserRepository userRepository, @Value("${service.jwt.secret-key}") String secretKey, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean verifyUser(String username, String role) {

        try{
            UserRole userRole = UserRole.valueOf(role);

            System.out.println("AuthServiceImpl 55line verifyUser userRole.toString() = " + userRole.toString());

            return userRepository.findByUsernameAndRoleAndIsDeleteFalse(username, userRole).isPresent();
        }catch(IllegalArgumentException e){
            return false;
        }

    }

    // TODO 임시로 void 형 받음, message 어떻게 할건지?
    @Transactional
    @Override
    public void signUp(UserSignUpReqDto userSignUpReqDto) {

        // 비밀번호 암호화
        String password = passwordEncoder.encode(userSignUpReqDto.getPassword());
        userSignUpReqDto.setPassword(password);

        userRepository.findByUsernameAndIsDeleteFalse(userSignUpReqDto.getUsername())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("중복된 ID가 존재합니다.");
                });

        userRepository.save(User.create(userSignUpReqDto));
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<?> signIn(UserSignInReqDto request) {

        User user = userRepository.findByUsernameAndIsDeleteFalse(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("없는 ID 입니다."));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", createAccessToken(user.getUsername(), user.getRole().getAuthority()));
        headers.set("Refresh-Authorization", createRefreshToken(user.getUsername(), user.getRole().getAuthority()));

        return new ResponseEntity<>("login success", headers, HttpStatus.OK);
    }


    // AccessToken 생성
    public String createAccessToken(String username, String role) {
        return BEARER + Jwts.builder()
                .claim("X-User-Id", username)
                .claim("X-Role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(String username, String role) {
        return BEARER + Jwts.builder()
                .subject("RefreshToken")
                .claim("X-User-Id", username)
                .claim("X-Role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }


}
