package com.logistics.hub.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistics.hub.domain.entity.Hub;
import com.logistics.hub.infrastructure.repository.HubRepositoryCustom;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
}
