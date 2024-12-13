package com.logistics.delivery.domain.entity;

import com.logistics.delivery.domain.common.BaseEntity;
import lombok.*;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name = "p_delivery_path")
public class DeliveryPath extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_path_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "hub_delivery_manager_id", nullable = false)
    private UUID hubDeliveryManagerId; // 허브 배송 담당자

    @Column(name = "hub_delivery_manager_sequence", nullable = false)
    private int hubDeliveryManagerSequence; // 허브 배송 담당자의 시퀀스

    @Column(name = "vendor_delivery_manager_id", nullable = true)
    private UUID vendorDeliveryManagerId;  //  업체 배송 담당자

    @Column(name = "vendor_delivery_manager_sequence ", nullable = true)
    private int vendorDeliveryManagerSequence; // 업체 배송 담당자의 시퀀스

    @Column(name = "source_hub_id", nullable = false)
    private UUID sourceHubId; // 도착허브

    @Column(name = "destination_hub_id", nullable = false)
    private UUID destinationHubId; // 도착 허브

    @Column(nullable = false)
    private double distance;  // 예상 거리

    @Column(name = "estimated_time", nullable = false)
    private LocalTime estimatedTime;   // 예상 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // 배송 상태

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    public void updateDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

}
