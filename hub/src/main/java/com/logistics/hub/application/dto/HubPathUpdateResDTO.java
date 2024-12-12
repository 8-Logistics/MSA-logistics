package com.logistics.hub.application.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.logistics.hub.domain.entity.HubPath;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathUpdateResDTO {
	private UUID pathId;
	private UUID sourceHubId;
	private UUID destinationHubId;
	private Double distance;
	private LocalTime estimatedTime;
	private LocalDateTime updatedAt;

	public static HubPathUpdateResDTO of(HubPath hubPath) {
		return HubPathUpdateResDTO.builder()
			.pathId(hubPath.getId())
			.sourceHubId(hubPath.getSourceHub().getId())
			.destinationHubId(hubPath.getDestinationHub().getId())
			.distance(hubPath.getDistance())
			.estimatedTime(hubPath.getEstimatedTime())
			.updatedAt(LocalDateTime.now())
			.build();
	}
}
