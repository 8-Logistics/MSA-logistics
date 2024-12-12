package com.logistics.hub;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.hub.application.dto.HubCreateReqDTO;
import com.logistics.hub.application.dto.HubCreateResDTO;
import com.logistics.hub.application.service.HubService;

@SpringBootTest
@AutoConfigureMockMvc
public class HubControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private HubService hubService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testCreateHub() throws Exception {
		// Given: 요청 데이터 및 Mock 응답 데이터 정의
		HubCreateReqDTO request = HubCreateReqDTO.builder()
			.name("Test Hub")
			.address("123 Test Street")
			.latitude(37.7749)
			.longitude(-122.4194)
			.build();

		HubCreateResDTO response = HubCreateResDTO.builder()
			.id(UUID.randomUUID())
			.name("Test Hub")
			.address("123 Test Street")
			.latitude(37.7749)
			.longitude(-122.4194)
			.build();

		// Mock 서비스 동작 정의
		Mockito.when(hubService.createHub(Mockito.any(HubCreateReqDTO.class)))
			.thenReturn(response);

		// When: MockMvc를 사용하여 POST 요청을 보냄
		mockMvc.perform(MockMvcRequestBuilders.post("/v1/hubs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk()) // 응답 상태가 200 OK인지 확인
			.andExpect(jsonPath("$.id").isNotEmpty()) // 응답에 ID 필드가 있는지 확인
			.andExpect(jsonPath("$.name").value("Test Hub")) // 응답 데이터의 이름 확인
			.andExpect(jsonPath("$.address").value("123 Test Street")) // 응답 데이터의 주소 확인
			.andExpect(jsonPath("$.latitude").value(37.7749)) // 응답 데이터의 위도 확인
			.andExpect(jsonPath("$.longitude").value(-122.4194)); // 응답 데이터의 경도 확인

		// Then: 서비스가 한 번 호출되었는지 확인
		Mockito.verify(hubService, Mockito.times(1)).createHub(Mockito.any(HubCreateReqDTO.class));
	}
}

