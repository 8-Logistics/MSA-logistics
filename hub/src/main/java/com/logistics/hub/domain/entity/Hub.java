package com.logistics.hub.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.logistics.hub.application.dto.HubCreateReqDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hub", schema = "hub_service")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hub {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "hub_id")
	private UUID id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "address", unique = true, nullable = false)
	private String address;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	@Column(name = "slack_id")
	private String slackId;

	@Column(name = "manager_name")
	private String managerName;

	@Column(name = "is_delete", nullable = false)
	private Boolean isDelete;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "source_hub_id")
	private List<HubPath> outboundPaths = new ArrayList<>();

	public static Hub create(HubCreateReqDTO dto) {
		return Hub.builder()
			.name(dto.getName())
			.address(dto.getAddress())
			.latitude(dto.getLatitude())
			.longitude(dto.getLongitude())
			.isDelete(false)
			.build();
	}

}
