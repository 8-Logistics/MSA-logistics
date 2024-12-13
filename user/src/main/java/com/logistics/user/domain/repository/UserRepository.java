package com.logistics.user.domain.repository;

import com.logistics.user.domain.entity.User;
import com.logistics.user.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndRoleAndIsDeleteFalse(String username, UserRole role);

    Optional<User> findByUsernameAndIsDeleteFalse(String username);

    Optional<User> findByIdAndIsDeleteFalse(Long id);

    Optional<User> findByIdAndRoleAndIsDeleteFalse(Long id, UserRole role);

    boolean existsBySlackIdAndIsDeleteFalseAndIdNot(String slackId, Long userId);

    boolean existsByEmailAndIsDeleteFalseAndIdNot(String email, Long id);
}
