package com.logistics.order.domain.repository;

import com.logistics.order.domain.entity.Slack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlackRepository extends JpaRepository<Slack, UUID> {
}
