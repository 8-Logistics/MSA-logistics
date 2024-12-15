package com.logistics.user.infrastructure.repository;

import com.logistics.user.application.dto.DeliveryManagerSearchReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryManagerCustomRepository {
    Page<DeliveryManagerSearchResDto> getDeliveryManagerSearch(
            DeliveryManagerSearchReqDto request, Pageable pageable, String username, String role);
}
