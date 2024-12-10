package com.logistics.hub.application.dto;

import java.time.LocalDateTime;
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
public class HubPathCreateResDTO {
	private UUID pathId;
	private UUID sourceHubId;
	private UUID destinationHubId;
	private Double distance;
	private LocalDateTime estimatedTime;
	private LocalDateTime createdAt;

	public static HubPathCreateResDTO of(HubPath hubPath) {
		return HubPathCreateResDTO.builder()
			.pathId(hubPath.getId())
			.sourceHubId(hubPath.getSourceHub().getId())
			.destinationHubId(hubPath.getDestinationHub().getId())
			.distance(hubPath.getDistance())
			.estimatedTime(hubPath.getEstimatedTime())
			.createdAt(LocalDateTime.now())
			.build();
	}
}
