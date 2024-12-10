package com.logistics.hub.application.service;

import org.springframework.stereotype.Service;

import com.logistics.hub.application.dto.HubCreateReqDTO;
import com.logistics.hub.application.dto.HubCreateResDTO;
import com.logistics.hub.domain.entity.Hub;
import com.logistics.hub.domain.repository.HubRepository;

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

}
