package com.logistics.delivery.infrastructure.repository;

import com.logistics.delivery.domain.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepositoryCustom {
    Page<Delivery> getDeliveries(String condition, String keyword, String status, Pageable pageable);
}
