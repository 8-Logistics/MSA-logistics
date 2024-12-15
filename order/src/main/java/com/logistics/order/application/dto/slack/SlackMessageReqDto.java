package com.logistics.order.application.dto.slack;

import com.logistics.order.domain.entity.Slack;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageReqDto {
    private String receiverId;
    private String text;

    public Slack toEntity(LocalDateTime sendTime) {
        return Slack.builder()
                .receiverId(this.receiverId)
                .message(this.text)
                .sendTime(sendTime)
                .build();
    }
}
