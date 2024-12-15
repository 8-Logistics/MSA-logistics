package com.logistics.vendor.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorCreateReqDTO {
	private String vendorName;
	private String vendorType;
	private UUID vendorHubId;
	private String vendorAddress;

}
