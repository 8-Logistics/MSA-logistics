package com.logistics.hub.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.logistics.hub.application.dto.HubCreateReqDTO;
import com.logistics.hub.application.dto.HubCreateResDTO;
import com.logistics.hub.application.dto.HubPathCreateReqDTO;
import com.logistics.hub.application.dto.HubPathCreateResDTO;
import com.logistics.hub.application.dto.HubUpdateReqDTO;
import com.logistics.hub.application.dto.HubUpdateResDTO;
import com.logistics.hub.domain.entity.Hub;
import com.logistics.hub.domain.entity.HubPath;
import com.logistics.hub.domain.repository.HubRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubService {
	private final HubRepository hubRepository;

	public HubCreateResDTO createHub(HubCreateReqDTO request) {
		Hub hub = Hub.create(request);
		hubRepository.save(hub);
		return HubCreateResDTO.of(hub);
	}

	@Transactional
	public HubUpdateResDTO updateHub(HubUpdateReqDTO request, UUID hubId, String userRole) {
		//to do : userRole 마스터 검증
		Hub hub = getHub(hubId);
		hub.update(request);
		return HubUpdateResDTO.of(hub);
	}

	public HubPathCreateResDTO createHubPath(HubPathCreateReqDTO request, UUID sourceHubId) {
		Hub sourceHub = getHub(sourceHubId);
		Hub destinationHub = getHub(request.getDestinationHubId());
		validateSourceAndDestination(sourceHub, destinationHub);
		HubPath newPath = new HubPath(sourceHub, destinationHub, request.getDistance(), request.getEstimatedTime());
		HubPath path = sourceHub.addOutboundPath(newPath);
		hubRepository.save(sourceHub);
		return HubPathCreateResDTO.of(path);
	}

	public Hub getHub(UUID id) {
		return hubRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Hub not found."));
	}

	private void validateSourceAndDestination(Hub sourceHub, Hub destinationHub) {
		if (sourceHub.equals(destinationHub)) {
			throw new IllegalArgumentException("Source hub and destination hub cannot be the same.");
		}
	}
}
