package com.logistics.user.application.dto;

import com.logistics.user.domain.entity.User;
import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResDto {

    private Long userId;
    private String username;
    private String email;
    private String slackId;
    private String name;
    private String role;
    private String userStatus;

    public static UserSearchResDto toUserResponse(User user){
        return UserSearchResDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .slackId(user.getSlackId())
                .name(user.getName())
                .role(user.getRole().toString())
                .userStatus(user.getUserStatus().toString())
                .build();
    }

}
