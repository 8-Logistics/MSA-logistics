package com.logistics.user.domain.entity;

import com.logistics.user.application.dto.UserSignUpReqDto;
import com.logistics.user.domain.enums.UserRole;
import com.logistics.user.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name = "p_user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;


    public static User create(UserSignUpReqDto request){
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .slackId(request.getSlackId())
                .name(request.getName())
                .role(UserRole.NORMAL)
                .userStatus(UserStatus.NONE)
                .build();

        return user;
    }

}
