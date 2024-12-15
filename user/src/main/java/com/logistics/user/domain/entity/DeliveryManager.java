package com.logistics.user.domain.entity;

import com.logistics.user.domain.enums.DeliveryManagerType;
import com.logistics.user.domain.enums.DeliveryStatus;
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
public class DeliveryManager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 명시
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryManagerType deliveryManagerType;

    @Column(nullable = true)
    private UUID sourceHubId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private long deliverySequence;

    public static DeliveryManager createDeliveryManager(User user, DeliveryManagerType type, UUID sourceHubId){
        return DeliveryManager.builder()
                .user(user)
                .deliveryManagerType(type)
                .sourceHubId(sourceHubId)
                .deliveryStatus(DeliveryStatus.PENDING_DELIVERY)
                .build();
    }

    public void updateDeliverySequence(){
        this.deliverySequence++;
    }

    public void updateDeliveryStatus(String deliveryStatus){

        if(DeliveryStatus.PENDING_DELIVERY.toString().equals(deliveryStatus)){
            this.deliveryStatus = DeliveryStatus.PENDING_DELIVERY;
        }else{
            this.deliveryStatus = DeliveryStatus.IN_DELIVERY;
        }

    }

    public void deliveryManagerTypeAndHubId(DeliveryManagerType deliveryManagerType, UUID sourceHubId){

        this.deliveryManagerType = deliveryManagerType;
        this.sourceHubId = sourceHubId;

    }

    public void resetDeliverySequence() {
        this.deliverySequence = 0;
    }
}
