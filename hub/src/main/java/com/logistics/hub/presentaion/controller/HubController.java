package com.logistics.hub.presentaion.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.hub.application.dto.HubCreateReqDTO;
import com.logistics.hub.application.dto.HubCreateResDTO;
import com.logistics.hub.application.dto.HubUpdateReqDTO;
import com.logistics.hub.application.dto.HubUpdateResDTO;
import com.logistics.hub.application.service.CustomPrincipal;
import com.logistics.hub.application.service.HubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HubController {
	private final HubService hubService;

	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PostMapping("/hubs")
	public ResponseEntity<HubCreateResDTO> createHub(
		@RequestBody HubCreateReqDTO request) {
		return ResponseEntity.ok(hubService.createHub(request));
	}

	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PutMapping("/hubs/{hubId}")
	public ResponseEntity<HubUpdateResDTO> updateHub(
		@PathVariable(name = "hubId") UUID hubId,
		@RequestBody HubUpdateReqDTO request) {
		return ResponseEntity.ok(hubService.updateHub(request, hubId));
	}

	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PutMapping("/hubs/{hubId}/manager")
	public ResponseEntity<String> assignHubManager(
		@PathVariable UUID hubId,
		@RequestParam String userId) {
		hubService.assignHubManager(hubId, userId);
		return ResponseEntity.ok("Assigned manager to hub Successful");
	}

	@PreAuthorize("hasAnyAuthority('MASTER')")
	@DeleteMapping("/hubs/{hubId}")
	public ResponseEntity<String> deleteHub(
		@AuthenticationPrincipal CustomPrincipal customPrincipal,
		@PathVariable(name = "hubId") UUID hubId) {
		hubService.deleteHub(hubId, customPrincipal.getUserId());
		return ResponseEntity.ok("Hub successfully deleted");
	}

}
