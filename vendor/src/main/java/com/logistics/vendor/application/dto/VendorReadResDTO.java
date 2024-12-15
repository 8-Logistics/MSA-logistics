package com.logistics.vendor.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.logistics.vendor.domain.entity.Vendor;
import com.logistics.vendor.domain.enums.VendorType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorReadResDTO {
	private UUID id;
	private String name;
	private VendorType vendorType;
	private UUID sourceHubId;
	private String address;
	private String managerId;
	private LocalDateTime updatedAt;

	public static VendorReadResDTO of(Vendor vendor) {
		return VendorReadResDTO.builder()
			.id(vendor.getId())
			.name(vendor.getName())
			.vendorType(vendor.getVendorType())
			.sourceHubId(vendor.getSourceHubId())
			.address(vendor.getAddress())
			.managerId(vendor.getManagerId())
			.updatedAt(vendor.getUpdatedAt())
			.build();
	}
}
