package com.logistics.hub.domain.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
	private int managerId;

	@OneToMany(cascade = CascadeType.ALL)
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
	}

	public void assignHubManager(int userId) {
		this.managerId = userId;
	}

	public void delete(String userId) {
		this.setDeletedBy(userId);
		this.setDeletedAt(LocalDateTime.now());
		this.setIsDeleted();
	}

	public Hub addOutboundPath(HubPath newPath) {
		this.outboundPaths.add(newPath);
		return this;
	}

	public HubPath updateOutboundPath(UUID pathId, Double newDistance, LocalTime newEstimatedTime) {
		HubPath path = findOutboundPathById(pathId);
		path.updatePath(newDistance, newEstimatedTime);
		return path;
	}

	public void removeOutboundPath(HubPath path, String userId) {
		// this.outboundPaths.remove(path);
		path.delete(userId);

	}

	public HubPath findOutboundPathById(UUID pathId) {
		return this.outboundPaths.stream()
			.filter(path -> path.getId().equals(pathId) && !path.isDelete())
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("HubPath not found with id: " + pathId));
	}

	public HubPath findPathById(UUID hubId) {
		return this.outboundPaths.stream()
			.filter(path -> path.getDestinationHub().getId().equals(hubId) && !path.isDelete())
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("HubPath not found with DestinationHubId: " + hubId));
	}
}
