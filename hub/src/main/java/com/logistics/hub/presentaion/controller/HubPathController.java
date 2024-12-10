package com.logistics.hub.presentaion.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.hub.application.dto.HubPathCreateReqDTO;
import com.logistics.hub.application.dto.HubPathCreateResDTO;
import com.logistics.hub.application.service.HubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HubPathController {
	private final HubService hubService;

	@PostMapping("/hubs/{sourceHubId}/paths")
	public ResponseEntity<HubPathCreateResDTO> addHubPath(
		@PathVariable UUID sourceHubId,
		@RequestBody HubPathCreateReqDTO request
	) {
		return ResponseEntity.ok(hubService.createHubPath(request, sourceHubId));
	}

}
