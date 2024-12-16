package com.logistics.order.application.dto;

import com.logistics.order.domain.entity.Order;
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
public class OrderSearchResDto {
    private OrderPage orderPage;

    public static OrderSearchResDto of(Page<Order> orderPage) {
        return OrderSearchResDto.builder()
                .orderPage(new OrderPage(orderPage))
                .build();
    }
    @Getter
    @ToString
    public static class OrderPage extends PagedModel<OrderPage.Order> {

        public OrderPage(Page<com.logistics.order.domain.entity.Order> orderPage) {
            super(
                    new PageImpl<>(
                            Order.from(orderPage.getContent()),
                            orderPage.getPageable(),
                            orderPage.getTotalElements()
                    )
            );
        }

        @Getter
        @Builder
        @AllArgsConstructor
        public static class Order {
            private UUID orderId;
            private UUID deliveryId;
            private UUID productId;
            private String pickupRequest;

            public static List<Order> from(List<com.logistics.order.domain.entity.Order> productList) {
                return productList.stream()
                        .map(Order::from)
                        .toList();
            }

            public static Order from(com.logistics.order.domain.entity.Order product) {
                return Order.builder()
                        .orderId(product.getOrderId())
                        .deliveryId(product.getProductId())
                        .productId(product.getProductId())
                        .pickupRequest(product.getPickupRequest())
                        .build();
            }
        }
    }
}
