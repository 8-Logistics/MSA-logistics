package com.logistics.hub.application.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
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

	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	@JsonProperty("estimatedTime")
	private LocalTime estimatedTime;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonProperty("createdAt")
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
