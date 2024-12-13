package com.logistics.product.application.dto;

import com.logistics.product.domain.entity.Product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {
    private int stock;
    private UUID productVendorId;
    public static OrderProductDto from(Product product) {
        return OrderProductDto.builder()
                .stock(product.getStock())
                .productVendorId(product.getVendorId())
                .build();
    }


}
