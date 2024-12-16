package com.logistics.user.application.service;

import com.logistics.user.application.dto.TokenDto;
import com.logistics.user.application.dto.UserSignInReqDto;
import com.logistics.user.application.dto.UserSignUpReqDto;
import com.logistics.user.domain.entity.User;
import com.logistics.user.domain.enums.UserRole;
import com.logistics.user.domain.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<String, String> redisTemplate;


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

    // TODO dto만들어 access, refreshtoken 담아 return 하기
    @Transactional(readOnly = true)
    @Override
    public TokenDto signIn(UserSignInReqDto request) {

        User user = userRepository.findByUsernameAndIsDeleteFalse(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("없는 ID 입니다."));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다");
        }

        String accessToken = tokenService.createAccessToken(user.getUsername(), user.getRole().getAuthority());
        String refreshToken = tokenService.createRefreshToken(user.getUsername(), user.getRole().getAuthority());

        return TokenDto.createTokenHeaders(accessToken, refreshToken);
    }

    @Override
    public TokenDto validateRefreshToken(String refreshToken) {

        // refreshToken 검증
        String usernameAndRole = tokenService.checkRefreshToken(refreshToken);

        String[] userInfo = usernameAndRole.split(",");

        if(userInfo[0] == null) {
            throw new IllegalArgumentException("Invalid refreshToken");
        }

        // redis안의 값이 같은지 확인
        if(!compareRefreshToken(userInfo[0], refreshToken)) {
            throw new IllegalArgumentException("Invalid refreshToken or no Token In DB");
        }

        String accessToken = tokenService.createAccessToken(userInfo[0], userInfo[1]);

        return TokenDto.createTokenHeaders(accessToken, null);
    }

    // Redis에 저장된 값을 가져와서 비교하는 메서드
    public boolean compareRefreshToken(String username, String refreshToken) {
        // Redis에서 저장된 refreshToken 가져오기
        String redisKey = "UserRefreshToken::" + username;
        String cachedToken = (String) redisTemplate.opsForValue().get(redisKey);

        if (cachedToken != null) {
            // Redis에서 가져온 값과 refresh 토큰 비교
            return cachedToken.equals(refreshToken);
        } else {
            // Redis에 값이 없으면 false 반환
            return false;
        }
    }


}
