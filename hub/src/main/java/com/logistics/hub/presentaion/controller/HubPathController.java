package com.logistics.hub.presentaion.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.hub.application.dto.HubPathCreateReqDTO;
import com.logistics.hub.application.dto.HubPathCreateResDTO;
import com.logistics.hub.application.dto.HubPathReadResDTO;
import com.logistics.hub.application.dto.HubPathUpdateReqDTO;
import com.logistics.hub.application.dto.HubPathUpdateResDTO;
import com.logistics.hub.application.service.CustomPrincipal;
import com.logistics.hub.application.service.HubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HubPathController {
	private final HubService hubService;

	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PostMapping("/hubs/{sourceHubId}/paths")
	public ResponseEntity<HubPathCreateResDTO> addHubPath(
		@PathVariable UUID sourceHubId,
		@RequestBody HubPathCreateReqDTO request
	) {
		return ResponseEntity.ok(hubService.createHubPath(request, sourceHubId));
	}

	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PutMapping("/hubs/{hubId}/paths/{pathId}")
	public ResponseEntity<HubPathUpdateResDTO> updateHubPath(
		@PathVariable(name = "hubId") UUID hubId,
		@PathVariable(name = "pathId") UUID pathId,
		@RequestBody HubPathUpdateReqDTO request
	) {
		return ResponseEntity.ok(hubService.updateHubPath(request, hubId, pathId));
	}

	@PreAuthorize("hasAnyAuthority('MASTER')")
	@DeleteMapping("/hubs/{hubId}/paths/{pathId}")
	public ResponseEntity<String> deleteHubPath(
		@AuthenticationPrincipal CustomPrincipal customPrincipal,
		@PathVariable(name = "hubId") UUID hubId,
		@PathVariable(name = "pathId") UUID pathId) {
		hubService.deleteHubPath(hubId, pathId, customPrincipal.getUserId());
		return ResponseEntity.ok("Path successfully deleted");
	}

	@GetMapping("/hubs/{hubId}/paths/{pathId}")
	public ResponseEntity<HubPathReadResDTO> getHubPath(
		@PathVariable(name = "hubId") UUID hubId,
		@PathVariable(name = "pathId") UUID pathId) {
		return ResponseEntity.ok(HubPathReadResDTO.of(hubService.getHubPath(hubId, pathId)));
	}

	@GetMapping("/hubs/path")
	public ResponseEntity<HubPathReadResDTO> getExactHubPath(
		@RequestParam(name = "sourceHubId") UUID sourceHubId,
		@RequestParam(name = "destinationHubId") UUID destinationHubId) {
		return ResponseEntity.ok(HubPathReadResDTO.of(hubService.getExactHubPath(sourceHubId, destinationHubId)));
	}

}
