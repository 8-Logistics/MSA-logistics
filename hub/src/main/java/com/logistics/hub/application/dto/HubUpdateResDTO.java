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
public class HubUpdateResDTO {
	private UUID id;
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
	private String slackId;
	private String managerName;
	private LocalDateTime updatedAt;

	public static HubUpdateResDTO of(Hub hub) {
		return HubUpdateResDTO.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.slackId(hub.getSlackId())
			.managerName(hub.getManagerName())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}
