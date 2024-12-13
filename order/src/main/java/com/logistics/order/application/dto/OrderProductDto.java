package com.logistics.order.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {
    private int stock;
    private UUID productVendorId; //공급업체
    private UUID productSourceHubId; //소속허브(출발허브)
}
