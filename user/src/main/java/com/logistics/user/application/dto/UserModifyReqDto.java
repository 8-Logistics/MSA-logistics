package com.logistics.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyReqDto {

    @NotBlank
    private String password;
    private String email;
    private String slackId;
    private String name;

}
