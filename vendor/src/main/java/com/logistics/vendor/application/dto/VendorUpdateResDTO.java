package com.logistics.vendor.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.logistics.vendor.domain.entity.Vendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorUpdateResDTO {
	private UUID id;
	private String vendorName;
	private String vendorType;
	private UUID vendorHubId;
	private String vendorAddress;
	private LocalDateTime updatedAt;

	public static VendorUpdateResDTO of(Vendor vendor) {
		return VendorUpdateResDTO.builder()
			.id(vendor.getId())
			.vendorName(vendor.getName())
			.vendorType(String.valueOf(vendor.getVendorType()))
			.vendorHubId(vendor.getSourceHubId())
			.vendorAddress(vendor.getAddress())
			.updatedAt(vendor.getUpdatedAt())
			.build();
	}
}
