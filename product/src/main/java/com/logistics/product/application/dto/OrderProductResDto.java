package com.logistics.product.application.dto;

import com.logistics.product.domain.entity.Product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResDto {
    private int stock;
    private UUID productVendorId;
    private UUID productSourceHubId;
    private String productName;

    public static OrderProductResDto from(Product product) {
        return OrderProductResDto.builder()
                .stock(product.getStock())
                .productVendorId(product.getVendorId())
                .productSourceHubId(product.getHubId())
                .productName(product.getName())
                .build();
    }


}
