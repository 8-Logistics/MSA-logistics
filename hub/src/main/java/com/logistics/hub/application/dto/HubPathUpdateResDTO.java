package com.logistics.hub.application.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
