package com.logistics.vendor.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.logistics.vendor.application.dto.VendorReadResDTO;
import com.logistics.vendor.domain.enums.SortOption;

public interface VendorRepositoryCustom {
	Page<VendorReadResDTO> searchVendors(UUID vendorId, Pageable pageable, String keyword, SortOption sortOption);
}
