package com.logistics.user.infrastructure.repository;

import com.logistics.user.application.dto.UserSearchReqDto;
import com.logistics.user.application.dto.UserSearchResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCustomRepository {
    Page<UserSearchResDto> searchUsers(UserSearchReqDto searchRequest, Pageable pageable);
}
