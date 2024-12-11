package com.logistics.product.application.dto;

import com.logistics.product.domain.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResDto {
    private UUID productId;
    private String productName;
    private UUID vendorId;
    private UUID hubId;
    private Long stock;
    private BigDecimal price;
    private String description;

    public static ProductResDto from(Product product) {
        return ProductResDto.builder()
                .productId(product.getProductId())
                .productName(product.getName())
                .vendorId(product.getVendorId())
                .hubId(product.getHubId())
                .stock(product.getStock())
                .description(product.getDescription())
                .build();
    }
}
