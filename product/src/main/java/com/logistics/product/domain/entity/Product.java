package com.logistics.product.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_products")
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name="vendor_id")
    private UUID vendorId;

    @Column(name="hub_id")
    private UUID hubId;

    @Column
    private Long stock;

    @Column
    private BigDecimal price;

    @Column
    private String description;

    @Builder.Default
    private boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    public void updateProduct(String name, Long stock, String description, BigDecimal price) {
        this.name = name;
        this.stock = stock;
        this.description = description;
        this.price = price;
    }

}
