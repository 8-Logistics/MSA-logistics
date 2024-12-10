package com.logistics.hub.domain.entity;

import java.time.LocalDateTime;
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
@Table(name = "hub_path", schema = "hub_service")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubPath {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "path_id")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_hub_id", nullable = false, insertable = false, updatable = false)
	private Hub sourceHub;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_hub_id", nullable = false)
	private Hub destinationHub;

	@Column(name = "distance", nullable = false)
	private Double distance;

	@Column(name = "estimated_time", nullable = false)
	private LocalDateTime estimatedTime;

	@Column(name = "is_delete", nullable = false)
	private Boolean isDelete;

	public HubPath(Hub sourceHub, Hub destinationHub, Double distance, LocalDateTime estimatedTime) {
		this.sourceHub = sourceHub;
		this.destinationHub = destinationHub;
		this.distance = distance;
		this.estimatedTime = estimatedTime;
		this.isDelete = false;
	}

}
