package com.logistics.vendor.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logistics.vendor.application.dto.UserRoleUpdateDto;
import com.logistics.vendor.application.dto.VendorCreateReqDTO;
import com.logistics.vendor.application.dto.VendorCreateResDTO;
import com.logistics.vendor.application.dto.VendorReadResDTO;
import com.logistics.vendor.application.dto.VendorUpdateReqDTO;
import com.logistics.vendor.application.dto.VendorUpdateResDTO;
import com.logistics.vendor.domain.entity.Vendor;
import com.logistics.vendor.domain.enums.SortOption;
import com.logistics.vendor.domain.repository.VendorRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorService {
	private final VendorRepository vendorRepository;
	private final UserService userService;
	private final HubService hubService;
	private final ProductService productService;

	public VendorCreateResDTO createVendor(VendorCreateReqDTO request) {
		isHubExists(request.getVendorHubId());
		Vendor vendor = Vendor.create(request);
		vendorRepository.save(vendor);
		return VendorCreateResDTO.of(vendor);
	}

	@Transactional
	public VendorUpdateResDTO updateVendor(VendorUpdateReqDTO request, UUID vendorId) {
		isHubExists(request.getVendorHubId());
		Vendor vendor = getVendor(vendorId);
		vendor.update(request);
		return VendorUpdateResDTO.of(vendor);
	}

	@Transactional
	public void assignVendorManager(UUID vendorId, int userId) {
		Vendor vendor = getVendor(vendorId);
		UserRoleUpdateDto dto = new UserRoleUpdateDto();
		dto.setVendorId(vendorId);
		try {
			userService.updateUserRole(userId, dto);
		} catch (FeignException e) {
			throw new IllegalArgumentException("Failed to update user role", e);
		}
		vendor.assignVendorManager(userId);
	}

	@Transactional
	public void deleteVendor(UUID vendorId, String userId) {
		Vendor vendor = getVendor(vendorId);
		vendor.delete(userId);
		try {
			productService.deleteProduct(vendorId);
		} catch (FeignException e) {
			throw new IllegalArgumentException("Failed to deleteProduct", e);
		}
	}

	public Vendor getVendor(UUID id) {
		return vendorRepository.findById(id).filter(vendor -> !vendor.isDelete())
			.orElseThrow(() -> new IllegalArgumentException("Vendor not found."));
	}

	public boolean isVendorExists(UUID id) {
		return vendorRepository.findById(id)
			.filter(vendor -> !vendor.isDelete())
			.isPresent();
	}

	public void isHubExists(UUID id) {
		if (!hubService.checkHub(id)) {
			throw new IllegalArgumentException("Hub is not found.");
		}
	}

	@Transactional(readOnly = true)
	public Page<VendorReadResDTO> searchVendors(UUID vendorId, int page, int size, String keyword,
		SortOption sortOption) {
		Pageable pageable = PageRequest.of(page, size);
		return vendorRepository.searchVendors(vendorId, pageable, keyword, sortOption);
	}

	// vendor address 조회 : FeignClient 호출 메서드
	@Transactional(readOnly = true)
	public String getVendorAddress(UUID vendorId) {
		Vendor vendor = getVendor(vendorId);
		return vendor.getAddress();
	}

}
