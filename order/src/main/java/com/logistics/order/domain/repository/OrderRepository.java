package com.logistics.order.domain.repository;

import com.logistics.order.domain.entity.Order;
import com.logistics.order.domain.entity.QOrder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.*;

public interface OrderRepository extends JpaRepository<Order, UUID>,
        QuerydslPredicateExecutor<Order>,
        QuerydslBinderCustomizer<QOrder> {
    @Override
    default void customize(QuerydslBindings querydslBindings, @NotNull QOrder qOrder) {
        querydslBindings.bind(String.class).all((StringPath path, Collection<? extends String> values) -> {
            List<String> valueList = new ArrayList<>(values.stream().map(String::trim).toList());
            if (valueList.isEmpty()) {
                return Optional.empty();
            }
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String s : valueList) {
                booleanBuilder.or(path.containsIgnoreCase(s));
            }
            return Optional.of(booleanBuilder);
        });
    }
    Optional<Object> findByOrderIdAndIsDeleteFalse(UUID orderId);
}
