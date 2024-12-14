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
public class HubReadResDto {
	private UUID id;
	private String name;
	private String address;
	private int managerId;
	private LocalDateTime createdAt;

	public static HubReadResDto of(Hub hub) {
		return HubReadResDto.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.managerId(hub.getManagerId())
			.createdAt(hub.getCreatedAt())
			.build();
	}
}
