package com.logistics.hub.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.logistics.hub.domain.entity.Hub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubCreateResDTO {
	private UUID id;
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
	private LocalDateTime createdAt;

	public static HubCreateResDTO of(Hub hub) {
		return HubCreateResDTO.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.createdAt(LocalDateTime.now())
			.build();
	}
}
