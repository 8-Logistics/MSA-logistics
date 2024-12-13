package com.logistics.hub.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.logistics.hub.application.dto.HubReadResDto;
import com.logistics.hub.domain.enums.SortOption;

public interface HubRepositoryCustom {
	public Page<HubReadResDto> searchHubs(UUID hubId, Pageable pageable, String keyword, SortOption sortOption);
}
