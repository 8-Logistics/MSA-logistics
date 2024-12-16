package com.logistics.user.domain.entity;

import com.logistics.user.application.dto.UserSignUpReqDto;
import com.logistics.user.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "p_user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

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


    public static User create(UserSignUpReqDto request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .slackId(request.getSlackId())
                .name(request.getName())
                .role(UserRole.NORMAL)
                .build();
    }

    public void userModify(String slackId, String name, String email){
        if(slackId != null && !slackId.isEmpty()){
            this.slackId = slackId;
        }
        if(name != null && !name.isEmpty()){
            this.name = name;
        }
        if(email != null && !email.isEmpty()){
            this.email = email;
        }

    }

    public void modifyRole(UserRole role){
        this.role = role;
    }



}
