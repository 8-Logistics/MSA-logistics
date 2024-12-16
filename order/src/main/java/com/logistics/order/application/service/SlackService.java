package com.logistics.order.application.service;

import com.logistics.order.application.dto.ai.OrderToAiReqDto;
import com.logistics.order.application.dto.slack.SlackMessageReqDto;
import com.logistics.order.domain.entity.Slack;
import com.logistics.order.domain.repository.SlackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SlackService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${slack.incoming-hook.url}")
    private String slackURL;

    private final AiMessage aiMessage;
    private final SlackRepository slackRepository;
    public void sendSlack(OrderToAiReqDto orderToAiReqDto) {

        // gemini 호출하여 메시지 내용 생성
        String message = aiMessage.getAiRequest(orderToAiReqDto);
        String deliveryManagerSlackId = orderToAiReqDto.getDeliveryManagerSlackId();

        SlackMessageReqDto slackMessageReqDto = new SlackMessageReqDto("@"+deliveryManagerSlackId,message);

        // message 내용 slack으로 전송 및 slack entity 저장
        try{
            restTemplate.postForObject(slackURL, slackMessageReqDto, String.class);
            Slack slack = slackMessageReqDto.toEntity(LocalDateTime.now());
            slackRepository.save(slack);
        }catch (Exception e){
            throw new IllegalArgumentException("slack failed");
        }

    }
}
