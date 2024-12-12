package com.logistics.user.application.service;

import com.logistics.user.application.dto.UserSearchResDto;
import com.logistics.user.domain.entity.User;
import com.logistics.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserSearchResDto findUser(Long userId) {

        User user = userRepository.findByIdAndIsDeleteFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("user Not Found"));

        return UserSearchResDto.toUserResponse(user);
    }
}
