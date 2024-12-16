package com.logistics.delivery.domain.entity;

import com.logistics.delivery.domain.common.BaseEntity;
import lombok.*;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name = "p_delivery")
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId; // 주문

    @Column(nullable = false)
    private UUID sourceHubId; // 출발 허브

    @Column(nullable = false)
    private UUID destinationHubId; // 도착 허브

    @Column(nullable = false)
    private String address; // 배송지 주소

    @Column(nullable = false)
    private String recipientName; // 주문인

    @Column(nullable = false)
    private String slackId; // 주문자 슬랙

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // 배송 상태

    @OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private DeliveryPath deliveryPath;

    public void addDeliveryPath(DeliveryPath deliveryPath) {
        this.deliveryPath = deliveryPath;
        deliveryPath.updateDelivery(this);
    }

    public void updateStatus(Status status) {
        this.status = status;
        this.deliveryPath.updateStatus(status);
    }
}
