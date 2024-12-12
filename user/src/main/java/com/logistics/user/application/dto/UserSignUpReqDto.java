package com.logistics.user.application.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpReqDto {

    @Pattern(
            regexp = "^[a-z0-9]{4,10}$",
            message = "입력값은 4자 이상 10자 이하의 소문자 알파벳과 숫자로만 구성되어야 합니다."
    )
    private String username;
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,15}$",
//            message = "비밀번호는 8자 이상 15자 이하이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다."
//    )
    private String password;
    private String email;
    private String slackId;
    private String name;

    public void setPassword(String password) {
        this.password = password;
    }

}
