package com.logistics.hub.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
