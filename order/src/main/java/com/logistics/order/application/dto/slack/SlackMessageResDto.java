package com.logistics.order.application.dto.slack;

import com.logistics.order.domain.entity.Slack;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageResDto {
    private String receiverId;
    private String message;
    private LocalDateTime sendTime;

    public static SlackMessageResDto from (Slack slack){
        return SlackMessageResDto.builder()
                .receiverId(slack.getReceiverId())
                .message(slack.getMessage())
                .sendTime(slack.getSendTime())
                .build();
    }
}