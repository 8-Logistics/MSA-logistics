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

import com.logistics.hub.application.dto.HubCreateReqDTO;
import com.logistics.hub.application.dto.HubCreateResDTO;
import com.logistics.hub.application.dto.HubReadResDto;
import com.logistics.hub.application.dto.HubUpdateReqDTO;
import com.logistics.hub.application.dto.HubUpdateResDTO;
import com.logistics.hub.application.service.CustomPrincipal;
import com.logistics.hub.application.service.HubService;
import com.logistics.hub.domain.enums.SortOption;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Hub-service API")
@RequestMapping("/api/v1")
public class HubController {
	private final HubService hubService;

	@Operation(summary = "허브 생성 ")
	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PostMapping("/hubs")
	public ResponseEntity<HubCreateResDTO> createHub(
		@RequestBody HubCreateReqDTO request) {
		return ResponseEntity.ok(hubService.createHub(request));
	}

	@Operation(summary = "허브 수정 ")
	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PutMapping("/hubs/{hubId}")
	public ResponseEntity<HubUpdateResDTO> updateHub(
		@PathVariable(name = "hubId") UUID hubId,
		@RequestBody HubUpdateReqDTO request) {
		return ResponseEntity.ok(hubService.updateHub(request, hubId));
	}

	@Operation(summary = "허브 담당자 등록(변경) ")
	@PreAuthorize("hasAnyAuthority('MASTER')")
	@PutMapping("/hubs/{hubId}/manager")
	public ResponseEntity<String> assignHubManager(
		@PathVariable UUID hubId,
		@RequestParam long userId) {
		hubService.assignHubManager(hubId, userId);
		return ResponseEntity.ok("Assigned manager to hub Successful");
	}

	@Operation(summary = "허브 삭제 ")
	@PreAuthorize("hasAnyAuthority('MASTER')")
	@DeleteMapping("/hubs/{hubId}")
	public ResponseEntity<String> deleteHub(
		@AuthenticationPrincipal CustomPrincipal customPrincipal,
		@PathVariable(name = "hubId") UUID hubId) {
		hubService.deleteHub(hubId, customPrincipal.getUserId());
		return ResponseEntity.ok("Hub successfully deleted");
	}

	@Operation(summary = "허브 단건 조회")
	@GetMapping("/hubs/{hubId}")
	public ResponseEntity<HubReadResDto> getHub(
		@PathVariable(name = "hubId") UUID hubId) {
		return ResponseEntity.ok(HubReadResDto.of(hubService.getHub(hubId)));
	}

	@Operation(summary = "허브 조회 검색")
	@GetMapping("/hubs")
	public ResponseEntity<Page<HubReadResDto>> searchHubs(
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "hubId", required = false) UUID hubId,
		@RequestParam(value = "keyword", required = false) String keyword,
		@RequestParam(value = "sortOption", required = false, defaultValue = "NAME_ASC") SortOption sortOption) {
		return ResponseEntity.ok(hubService.searchHubs(hubId, page - 1, size, keyword, sortOption));
	}

	@Operation(summary = "허브 확인")
	@GetMapping("/hubs/{hubId}/exists")
	public boolean checkHub(@PathVariable("hubId") UUID hubId) {
		return hubService.checkHub(hubId);
	}

	@Operation(summary = "User ID로 담당허브 조회")
	@GetMapping("/hubs/user/{userId}")
	public UUID getUserHubId(@PathVariable("userId") long userId) {
		log.info("getUserHubId Controller");
		return hubService.getUserHubId(userId);
	}

}
