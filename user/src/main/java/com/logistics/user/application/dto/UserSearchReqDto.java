package com.logistics.user.application.dto;

import com.logistics.user.domain.enums.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchReqDto {

    private List<Long> userIdList;
    private String username;
    private String email;
    private String slackId;
    private String name;
    private UserRole role;
    private String keyword; // 검색 키워드
    private Boolean isDelete; // 삭제여부 검색
}
