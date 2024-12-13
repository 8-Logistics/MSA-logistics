package com.logistics.delivery.domain.repository;

import com.logistics.delivery.domain.entity.DeliveryPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryPathRepository extends JpaRepository<DeliveryPath, UUID> {
    Optional<DeliveryPath> findTopBySourceHubIdOrderByCreatedAtDesc(UUID sourceHubId);
}
