package com.logistics.product.application.dto;

import com.logistics.product.domain.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductReqDto {

        private String name;
        private UUID vendorId;
        private UUID hubId;
        private int stock;
        private BigDecimal price;
        private String description;

        public static Product toProduct(ProductReqDto reqDto) {
                return Product.builder()
                        .name(reqDto.getName())
                        .vendorId(reqDto.getVendorId())
                        .hubId(reqDto.getHubId())
                        .stock(reqDto.getStock())
                        .price(reqDto.getPrice())
                        .description(reqDto.getDescription())
                        .build();
        }
}
