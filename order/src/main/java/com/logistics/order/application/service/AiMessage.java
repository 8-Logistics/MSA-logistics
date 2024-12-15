package com.logistics.order.application.service;

import com.logistics.order.application.dto.ai.OrderToAiReqDto;
import com.logistics.order.application.dto.ai.AiReqDto;
import com.logistics.order.application.dto.ai.AiResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiMessage {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;
    private static final String REQ_MESSAGE = "| 기준으로 데이터를 구분해서 주문 번호, 주문자 정보, 상품 정보, 요청사항, 발송지, 도착지, 배송담당자 순으로 한 줄씩 : 로 정리해주고," +
            " 출발 주소와 도착지 주소 거리를 계산해서 요청사항 날짜에 받을 수 있는 최종 발송시한은 몇월 며칠 몇시 입니다. 라고 알려줘";

    public String getAiRequest(OrderToAiReqDto req) {

        //Todo req 완성 후 채우기
        String prompt = req.getOrderId() + "|" + req.getPickupRequest() + "|" + req.getProductVendorAddress();

        String requestUrl = geminiApiUrl + "?key=" + geminiApiKey;

        AiReqDto request = new AiReqDto(prompt + REQ_MESSAGE);
        AiResDto response = restTemplate.postForObject(requestUrl, request, AiResDto.class);

        String responseMessage = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();
        return responseMessage;
    }

}
