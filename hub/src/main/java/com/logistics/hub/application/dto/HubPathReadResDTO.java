package com.logistics.hub.application.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.logistics.hub.domain.entity.HubPath;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathReadResDTO {
	private UUID pathId;
	private UUID sourceHubId;
	private UUID destinationHubId;
	private Double distance;
	private LocalTime estimatedTime;
	private LocalDateTime createdAt;

	public static HubPathReadResDTO of(HubPath hubPath) {
		return HubPathReadResDTO.builder()
			.pathId(hubPath.getId())
			.sourceHubId(hubPath.getSourceHub().getId())
			.destinationHubId(hubPath.getDestinationHub().getId())
			.distance(hubPath.getDistance())
			.estimatedTime(hubPath.getEstimatedTime())
			.createdAt(LocalDateTime.now())
			.build();
	}
}
