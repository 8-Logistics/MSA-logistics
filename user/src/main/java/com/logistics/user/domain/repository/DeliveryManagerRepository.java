package com.logistics.user.domain.repository;

import com.logistics.user.domain.entity.DeliveryManager;
import com.logistics.user.infrastructure.repository.DeliveryManagerCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID>, DeliveryManagerCustomRepository {


    Optional<DeliveryManager> findByUserIdAndIsDeleteFalse(Long userId);

    Optional<DeliveryManager> findByIdAndIsDeleteFalse(UUID deliveryId);

    Optional<DeliveryManager> findTopBySourceHubIdIsNullAndIsDeleteFalseOrderByDeliverySequenceAscCreatedAtAsc();

    Optional<DeliveryManager> findTopBySourceHubIdAndIsDeleteFalseOrderByDeliverySequenceAscCreatedAtAsc(UUID hubId);
}
