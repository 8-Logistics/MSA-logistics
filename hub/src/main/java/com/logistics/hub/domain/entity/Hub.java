package com.logistics.hub.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.logistics.hub.application.dto.HubCreateReqDTO;
import com.logistics.hub.application.dto.HubUpdateReqDTO;

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
@Table(name = "p_hub")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hub extends BaseEntity {
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

	@Column(name = "manager_id")
	private String managerId;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "source_hub_id")
	private List<HubPath> outboundPaths = new ArrayList<>();

	public static Hub create(HubCreateReqDTO dto) {
		return Hub.builder()
			.name(dto.getName())
			.address(dto.getAddress())
			.latitude(dto.getLatitude())
			.longitude(dto.getLongitude())
			.build();
	}

	public void update(HubUpdateReqDTO request) {
		if (request.getName() != null) {
			this.name = request.getName();
		}
		if (request.getAddress() != null) {
			this.address = request.getAddress();
		}
		if (request.getLatitude() != null) {
			this.latitude = request.getLatitude();
		}
		if (request.getLongitude() != null) {
			this.longitude = request.getLongitude();
		}
		if (request.getManagerId() != null) {
			this.managerId = request.getManagerId();
		}
	}

	public void delete(String userId) {
		this.setDeletedBy(userId);
		this.setDeletedAt(LocalDateTime.now());
		this.setIsDeleted();
	}

	public HubPath addOutboundPath(HubPath path) {
		this.outboundPaths.add(path);
		return path;
	}

}