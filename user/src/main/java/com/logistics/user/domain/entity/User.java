package com.logistics.user.domain.entity;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String email; // user_name 으로 컬럼이 들어간다.
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.NORMAL;

    // TODO slackID UUID가 아니고 varchar 형 맞는지 체크 해야된다.
    private String slackId;








}
