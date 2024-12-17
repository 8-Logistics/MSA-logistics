package com.logistics.vendor.presentaion.controller;

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

import com.logistics.vendor.application.dto.VendorCreateReqDTO;
import com.logistics.vendor.application.dto.VendorCreateResDTO;
import com.logistics.vendor.application.dto.VendorReadResDTO;
import com.logistics.vendor.application.dto.VendorUpdateReqDTO;
import com.logistics.vendor.application.dto.VendorUpdateResDTO;
import com.logistics.vendor.application.service.CustomPrincipal;
import com.logistics.vendor.application.service.VendorService;
import com.logistics.vendor.domain.enums.SortOption;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Vendor-Service API", description = "업체 API Controller")
public class VendorController {
	private final VendorService vendorService;

	@Operation(summary = "업체 생성 ")
	@PreAuthorize("hasAnyAuthority('MASTER','HUB_MANAGER')")
	@PostMapping("/vendors")
	public ResponseEntity<VendorCreateResDTO> createVendor(
		@RequestBody VendorCreateReqDTO request) {
		return ResponseEntity.ok(vendorService.createVendor(request));
	}

	@Operation(summary = "업체 수정 ")
	@PreAuthorize("hasAnyAuthority('MASTER','HUB_MANAGER','VENDOR_MANAGER')")
	@PutMapping("/vendors/{vendorId}")
	public ResponseEntity<VendorUpdateResDTO> updateVendor(
		@PathVariable(name = "vendorId") UUID vendorId,
		@RequestBody VendorUpdateReqDTO request) {
		return ResponseEntity.ok(vendorService.updateVendor(request, vendorId));
	}

	@Operation(summary = "업체 담당매니저 등록(변경) ")
	@PreAuthorize("hasAnyAuthority('MASTER','HUB_MANAGER')")
	@PutMapping("/vendors/{vendorId}/manager")
	public ResponseEntity<String> assignVendorManager(
		@PathVariable UUID vendorId,
		@RequestBody long userId) {
		vendorService.assignVendorManager(vendorId, userId);
		return ResponseEntity.ok("Assigned manager to Vendor Successful");
	}

	@Operation(summary = "업체 삭제 ")
	@PreAuthorize("hasAnyAuthority('MASTER','HUB_MANAGER')")
	@DeleteMapping("/vendors/{vendorId}")
	public ResponseEntity<String> deleteVendor(
		@AuthenticationPrincipal CustomPrincipal customPrincipal,
		@PathVariable(name = "vendorId") UUID vendorId) {
		vendorService.deleteVendor(vendorId, customPrincipal.getUserId());
		return ResponseEntity.ok("Vendor successfully deleted");
	}

	@Operation(summary = "업체 단건 조회 ")
	@GetMapping("/vendors/{vendorId}")
	public ResponseEntity<VendorReadResDTO> getVendor(
		@PathVariable(name = "vendorId") UUID vendorId) {
		return ResponseEntity.ok(VendorReadResDTO.of(vendorService.getVendor(vendorId)));
	}

	@Operation(summary = "업체 검색 조회 ")
	@GetMapping("/vendors")
	public ResponseEntity<Page<VendorReadResDTO>> searchVendors(
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "vendorId", required = false) UUID vendorId,
		@RequestParam(value = "keyword", required = false) String keyword,
		@RequestParam(value = "sortOption", required = false, defaultValue = "NAME_ASC") SortOption sortOption) {
		return ResponseEntity.ok(vendorService.searchVendors(vendorId, page - 1, size, keyword, sortOption));
	}

	@Operation(summary = "업체 주소 조회 ")
	@GetMapping("/vendors/{vendorId}/address")
	public ResponseEntity<String> getVendorAddress(
		@PathVariable(name = "vendorId") UUID vendorId) {
		return ResponseEntity.ok(vendorService.getVendorAddress(vendorId));
	}

	@Operation(summary = "업체 삭제여부 확인 ")
	@GetMapping("/vendors/vendor/{vendorId}")
	public boolean checkVendor(@PathVariable(name="vendorId") UUID vendorId) {
		return vendorService.checkVendor(vendorId);
	}

}
