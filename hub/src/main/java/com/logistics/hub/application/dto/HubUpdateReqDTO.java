package com.logistics.hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubUpdateReqDTO {
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
	private String slackId;
	private String managerName;
}
