package com.logistics.user.domain.repository;

import com.logistics.user.domain.entity.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {


    Optional<DeliveryManager> findByUserIdAndIsDeleteFalse(Long userId);

}
