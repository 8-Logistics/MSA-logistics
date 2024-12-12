package com.logistics.hub.domain.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_hub_path")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPath extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "path_id")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_hub_id", nullable = false)
	private Hub sourceHub;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_hub_id", nullable = false)
	private Hub destinationHub;

	@Column(name = "distance", nullable = false)
	private Double distance;

	@Column(name = "estimated_time", nullable = false)
	private LocalTime estimatedTime;

	public HubPath(Hub sourceHub, Hub destinationHub, Double distance, LocalTime estimatedTime) {
		this.sourceHub = sourceHub;
		this.destinationHub = destinationHub;
		this.distance = distance;
		this.estimatedTime = estimatedTime;
	}

	public void updatePath(Double newDistance, LocalTime newEstimatedTime) {
		if (newDistance != null) {
			this.distance = newDistance;
		}
		if (newEstimatedTime != null) {
			this.estimatedTime = newEstimatedTime;
		}
	}

	public void delete(String userId) {
		this.setDeletedBy(userId);
		this.setDeletedAt(LocalDateTime.now());
		this.setIsDeleted();
	}
}
