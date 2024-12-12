package com.logistics.user.domain.repository;

import com.logistics.user.domain.entity.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {
}
