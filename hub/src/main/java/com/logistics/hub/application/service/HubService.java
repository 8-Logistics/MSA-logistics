package com.logistics.hub.application.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logistics.hub.application.dto.HubCreateReqDTO;
import com.logistics.hub.application.dto.HubCreateResDTO;
import com.logistics.hub.application.dto.HubPathCreateReqDTO;
import com.logistics.hub.application.dto.HubPathCreateResDTO;
import com.logistics.hub.application.dto.HubPathUpdateReqDTO;
import com.logistics.hub.application.dto.HubPathUpdateResDTO;
import com.logistics.hub.application.dto.HubReadResDto;
import com.logistics.hub.application.dto.HubUpdateReqDTO;
import com.logistics.hub.application.dto.HubUpdateResDTO;
import com.logistics.hub.application.dto.UserRoleUpdateDto;
import com.logistics.hub.domain.entity.Hub;
import com.logistics.hub.domain.entity.HubPath;
import com.logistics.hub.domain.enums.SortOption;
import com.logistics.hub.domain.repository.HubRepository;
import com.logistics.hub.infrastructure.client.UserClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService {
	private final HubRepository hubRepository;
	private final UserService userService;

	@CachePut(cacheNames = "hubCache", key = "#result.id")
	public HubCreateResDTO createHub(HubCreateReqDTO request) {
		Hub hub = Hub.create(request);
		hubRepository.save(hub);
		return HubCreateResDTO.of(hub);
	}

	@Transactional
	@CachePut(cacheNames = "hubCache", key = "args[0]")
	@Caching(evict = {
		@CacheEvict(cacheNames = "hubReadCache", allEntries = true), // 허브목록 캐시 전체 삭제
		@CacheEvict(cacheNames = "hubPathReadCache", allEntries = true) // 허브패스목록 전체 삭제
	})
	public HubUpdateResDTO updateHub(HubUpdateReqDTO request, UUID hubId) {
		Hub hub = getHub(hubId);
		hub.update(request);
		return HubUpdateResDTO.of(hub);
	}

	@Transactional
	public void assignHubManager(UUID hubId, long userId) {
		Hub hub = getHub(hubId);
		UserRoleUpdateDto dto = new UserRoleUpdateDto();
		dto.setSourceHubId(hubId);
		try {
			userService.updateUserRole(userId, dto);
		} catch (FeignException e) {
			throw new IllegalArgumentException("Failed to update user role", e);
		}
		hub.assignHubManager(userId);
	}

	@Transactional
	@Caching(evict = {
		@CacheEvict(cacheNames = "hubReadCache", allEntries = true), // 허브목록 캐시 전체 삭제
		@CacheEvict(cacheNames = "hubPathReadCache", allEntries = true) // 허브패스목록 전체 삭제
	})
	public void deleteHub(UUID hubId, String userId) {
		Hub hub = getHub(hubId);
		hub.delete(userId);
	}

	@Transactional
	@CacheEvict(cacheNames = "hubPathReadCache", key = "#sourceHubId")
	public HubPathCreateResDTO createHubPath(HubPathCreateReqDTO request, UUID sourceHubId) {
		Hub sourceHub = getHub(sourceHubId);
		Hub destinationHub = getHub(request.getDestinationHubId());
		validateSourceAndDestination(sourceHub, destinationHub);
		isPathExist(sourceHub, destinationHub);
		HubPath newPath = new HubPath(UUID.randomUUID(), sourceHub, destinationHub, request.getDistance(),
			request.getEstimatedTime());
		Hub newHub = sourceHub.addOutboundPath(newPath);
		hubRepository.save(newHub);
		return HubPathCreateResDTO.of(newPath);
	}

	@Transactional
	@CachePut(cacheNames = "hubPathCache", key = "args[0]")
	@CacheEvict(cacheNames = "hubPathReadCache", key = "#pathId")
	public HubPathUpdateResDTO updateHubPath(HubPathUpdateReqDTO request, UUID hubId, UUID pathId) {
		Hub sourceHub = getHub(hubId);
		HubPath path = sourceHub.updateOutboundPath(pathId, request.getDistance(), request.getEstimatedTime());
		return HubPathUpdateResDTO.of(path);
	}

	@Transactional
	@Caching(evict = {
		@CacheEvict(cacheNames = "hubPathReadCache", key = "#hubId"), // 해당 허브패스 캐시 삭제
		@CacheEvict(cacheNames = "hubPathReadAllCache", allEntries = true) // 허브패스목록 전체 삭제
	})
	public void deleteHubPath(UUID hubId, UUID pathId, String userId) {
		Hub hub = getHub(hubId);
		HubPath hubPath = hub.findOutboundPathById(pathId);
		hub.removeOutboundPath(hubPath, userId);
	}

	public Hub getHub(UUID id) {
		log.info("###Hub checkHub Service getHub");
		return hubRepository.findById(id).filter(hub -> !hub.isDelete())
			.orElseThrow(() -> new IllegalArgumentException("Hub not found."));
	}

	public void validateSourceAndDestination(Hub sourceHub, Hub destinationHub) {
		if (sourceHub.equals(destinationHub)) {
			throw new IllegalArgumentException("Source hub and destination hub cannot be the same.");
		}
	}

	public void isPathExist(Hub sourceHub, Hub destinationHub) {
		if (sourceHub.getOutboundPaths().stream()
			.anyMatch(hubPath -> hubPath.getDestinationHub().equals(destinationHub))) {
			throw new IllegalArgumentException("This hubPath is already exist.");
		}
	}

	@Cacheable(cacheNames = "hubPathReadCache", key = "#sourceHubId + ':' + #destinationHubId")
	public HubPath getHubPath(UUID hubId, UUID pathId) {
		Hub hub = getHub(hubId);
		return hub.findOutboundPathById(pathId);
	}

	@Transactional(readOnly = true)
	public Page<HubReadResDto> searchHubs(UUID hubId, int page, int size, String keyword, SortOption sortOption) {
		Pageable pageable = PageRequest.of(page, size);
		return hubRepository.searchHubs(hubId, pageable, keyword, sortOption);
	}

	// 특정허브경로 정보 조회 : FeignClient 호출 메서드
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = "hubPathReadCache", key = "#sourceHubId + ':' + #destinationHubId")
	public HubPath getExactHubPath(UUID sourceHubId, UUID destinationHubId) {
		Hub sourceHub = getHub(sourceHubId);
		Hub destinationHub = getHub(destinationHubId);
		validateSourceAndDestination(sourceHub, destinationHub);
		return sourceHub.findPathById(destinationHubId);
	}

	// 허브 있는지 체크 : FeignClient 호출 메서드
	public boolean checkHub(UUID hubId) {
		log.info("###Hub checkHub Service checkHub");
		return getHub(hubId) != null;
	}

	// 허브 매니저ID로 소속허브ID 확인: FeignClient 호출 메서드
	public UUID getUserHubId(long userId) {
		log.info("getUserHubId Service");
		return hubRepository.findAll().stream()
			.filter(hub -> hub.getManagerId() == userId)
			.map(Hub::getId)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("No hub found for userId: " + userId));
	}

}
