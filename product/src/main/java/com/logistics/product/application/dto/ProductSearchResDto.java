package com.logistics.product.application.dto;

import com.logistics.product.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ProductSearchResDto {

    private ProductPage productPage;

    public static ProductSearchResDto of(Page<Product> productPage) {
        return ProductSearchResDto.builder()
                .productPage(new ProductPage(productPage))
                .build();
    }

    @Getter
    @ToString
    public static class ProductPage extends PagedModel<ProductPage.Product> {

        public ProductPage(Page<com.logistics.product.domain.entity.Product> productPage) {
            super(
                    new PageImpl<>(
                            Product.from(productPage.getContent()),
                            productPage.getPageable(),
                            productPage.getTotalElements()
                    )
            );
        }

        @Getter
        @Builder
        @AllArgsConstructor
        public static class Product {
            private UUID id;
            private String name;
            private int stock;
            private BigDecimal price;
            private String description;

            public static List<Product> from(List<com.logistics.product.domain.entity.Product> productList) {
                return productList.stream()
                        .map(Product::from)
                        .toList();
            }

            public static Product from(com.logistics.product.domain.entity.Product product) {
                return Product.builder()
                        .id(product.getProductId())
                        .name(product.getName())
                        .stock(product.getStock())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .build();
            }

        }

    }

}
