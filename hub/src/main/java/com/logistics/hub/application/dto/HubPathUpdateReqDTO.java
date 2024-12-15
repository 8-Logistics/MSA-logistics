package com.logistics.hub.application.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPathUpdateReqDTO {
	private Double distance;
	private LocalTime estimatedTime;
}
