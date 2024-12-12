package com.logistics.user.application.dto;

import com.logistics.user.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateReqDto {

    private String password;
    private String email;
    private String slackId;
    private String name;
    private UserRole role;

}
