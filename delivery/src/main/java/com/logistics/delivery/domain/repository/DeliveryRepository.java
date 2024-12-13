package com.logistics.delivery.domain.repository;

import com.logistics.delivery.domain.entity.Delivery;
import com.logistics.delivery.infrastructure.repository.DeliveryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> , DeliveryRepositoryCustom {
    Optional<Delivery> findByIdAndIsDeleteFalse(UUID deliveryId);
}
