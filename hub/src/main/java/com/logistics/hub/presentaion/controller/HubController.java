package com.logistics.hub.presentaion.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.hub.application.dto.HubCreateReqDTO;
import com.logistics.hub.application.dto.HubCreateResDTO;
import com.logistics.hub.application.dto.HubUpdateReqDTO;
import com.logistics.hub.application.dto.HubUpdateResDTO;
import com.logistics.hub.application.service.HubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HubController {
	private final HubService hubService;

	@PostMapping("/hubs")
	public ResponseEntity<HubCreateResDTO> createHub(
		@RequestBody HubCreateReqDTO request) {
		return ResponseEntity.ok(hubService.createHub(request));
	}

	@PutMapping("/hubs/{hubId}")
	public ResponseEntity<HubUpdateResDTO> updateHub(
		@RequestHeader("X-User-role") String userRole,
		@PathVariable(name = "hubId") UUID hubId,
		@RequestBody HubUpdateReqDTO request) {
		return ResponseEntity.ok(hubService.updateHub(request, hubId, userRole));
	}

}
