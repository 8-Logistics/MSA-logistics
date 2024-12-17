package com.logistics.hub.presentaion.controller;

import java.util.UUID;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "HubPath-Service API", description = "허브간 경로 API Controller")
public class HubPathController {
	private final HubService hubService;

	@Operation(summary = "허브간 경로 생성")
	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PostMapping("/hubs/{sourceHubId}/paths")
	public ResponseEntity<HubPathCreateResDTO> addHubPath(
		@PathVariable UUID sourceHubId,
		@RequestBody HubPathCreateReqDTO request
	) {
		return ResponseEntity.ok(hubService.createHubPath(request, sourceHubId));
	}

	@Operation(summary = "허브간 경로 수정")
	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PutMapping("/hubs/{hubId}/paths/{pathId}")
	public ResponseEntity<HubPathUpdateResDTO> updateHubPath(
		@PathVariable(name = "hubId") UUID hubId,
		@PathVariable(name = "pathId") UUID pathId,
		@RequestBody HubPathUpdateReqDTO request
	) {
		return ResponseEntity.ok(hubService.updateHubPath(request, hubId, pathId));
	}

	@Operation(summary = "허브간 경로 삭제")
	@PreAuthorize("hasAnyAuthority('MASTER')")
	@DeleteMapping("/hubs/{hubId}/paths/{pathId}")
	public ResponseEntity<String> deleteHubPath(
		@AuthenticationPrincipal CustomPrincipal customPrincipal,
		@PathVariable(name = "hubId") UUID hubId,
		@PathVariable(name = "pathId") UUID pathId) {
		hubService.deleteHubPath(hubId, pathId, customPrincipal.getUserId());
		return ResponseEntity.ok("Path successfully deleted");
	}

	@Operation(summary = "허브간 경로 단건 조회(By ID)")
	@GetMapping("/hubs/{hubId}/paths/{pathId}")
	public ResponseEntity<HubPathReadResDTO> getHubPath(
		@PathVariable(name = "hubId") UUID hubId,
		@PathVariable(name = "pathId") UUID pathId) {
		return ResponseEntity.ok(HubPathReadResDTO.of(hubService.getHubPath(hubId, pathId)));
	}

	@Operation(summary = "허브간 경로 단건 조회(By 출발허브, 도착허브)")
	@GetMapping("/hubs/path")
	public ResponseEntity<HubPathReadResDTO> getExactHubPath(
		@RequestParam(name = "sourceHubId") UUID sourceHubId,
		@RequestParam(name = "destinationHubId") UUID destinationHubId) {
		return ResponseEntity.ok(HubPathReadResDTO.of(hubService.getExactHubPath(sourceHubId, destinationHubId)));
	}

}
