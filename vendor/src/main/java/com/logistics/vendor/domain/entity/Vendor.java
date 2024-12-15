package com.logistics.vendor.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.logistics.vendor.application.dto.VendorCreateReqDTO;
import com.logistics.vendor.application.dto.VendorUpdateReqDTO;
import com.logistics.vendor.domain.enums.VendorType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_vendor")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "vendor_id")
	private UUID id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "vendor_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private VendorType vendorType;

	@Column(name = "source_hub_id", nullable = false)
	private UUID sourceHubId;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "manager_id")
	private String managerId;

	public static Vendor create(VendorCreateReqDTO dto) {
		return Vendor.builder()
			.name(dto.getVendorName())
			.vendorType(VendorType.valueOf(dto.getVendorType()))
			.sourceHubId(dto.getVendorHubId())
			.address(dto.getVendorAddress())
			.build();
	}

	public void update(VendorUpdateReqDTO request) {
		if (request.getVendorName() != null) {
			this.name = request.getVendorName();
		}
		if (request.getVendorType() != null) {
			this.vendorType = VendorType.valueOf(request.getVendorType());
		}
		if (request.getVendorHubId() != null) {
			this.sourceHubId = request.getVendorHubId();
		}
		if (request.getVendorAddress() != null) {
			this.address = request.getVendorAddress();
		}
	}

	public void assignVendorManager(String userId) {
		this.managerId = userId;
	}

	public void delete(String userId) {
		this.setDeletedBy(userId);
		this.setDeletedAt(LocalDateTime.now());
		this.setIsDeleted();
	}

}
