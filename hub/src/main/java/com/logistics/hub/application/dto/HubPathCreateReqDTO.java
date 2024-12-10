package com.logistics.hub.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathCreateReqDTO {
	private UUID destinationHubId;
	private Double distance;
	private LocalDateTime estimatedTime;

}
