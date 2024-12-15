package com.logistics.vendor.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VendorType {
	PRODUCT_VENDOR("Product Vendor"),
	CONSUMER_VENDOR("Consumer Vendor");

	private final String description;
}
