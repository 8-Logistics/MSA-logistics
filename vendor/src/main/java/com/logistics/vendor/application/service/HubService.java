package com.logistics.vendor.application.service;

import java.util.UUID;

// 응용 계층 DIP 적용을 위한 유저 서비스 인터페이스
public interface HubService {
	public boolean checkHub(UUID hubId);
}
