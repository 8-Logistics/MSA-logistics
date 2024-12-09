package com.logistics.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpReqDto {

    private String username;
    private String password;
    private String email;
    private String slackId;
    private String name;

}
