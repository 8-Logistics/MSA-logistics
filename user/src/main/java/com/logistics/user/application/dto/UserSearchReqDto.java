package com.logistics.user.application.dto;

import com.logistics.user.domain.enums.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchReqDto {

    private List<Long> userIdList;
    private UserRole role;
    private String keyword; // 검색 키워드
    private Boolean isDelete; // 삭제여부 검색
}
