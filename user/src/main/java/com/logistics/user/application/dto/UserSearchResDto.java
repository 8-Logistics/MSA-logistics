package com.logistics.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResDto {

    private Long userId;
    private String username;
    private String email;
    private String slackId;
    private String name;
    private String role;

}
