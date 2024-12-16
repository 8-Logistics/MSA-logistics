package com.logistics.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_slacks")
public class Slack{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="slack_id")
    private UUID slackId;

    @Column(name="recipient_id")
    private String receiverId;

    @Column
    private String message;

    @Column
    private LocalDateTime sendTime;

}
