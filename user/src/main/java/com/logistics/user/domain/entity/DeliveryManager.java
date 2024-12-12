package com.logistics.user.domain.entity;

import com.logistics.user.domain.enums.DeliveryManagerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Table(name = "p_delivery_manager")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryManagerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 명시
    private User user;

    @Enumerated(EnumType.STRING)
    private DeliveryManagerType deliveryManagerType;

    private UUID sourceHubId;

    private Integer deliverySequence;
}
