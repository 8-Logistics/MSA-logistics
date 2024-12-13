package com.logistics.delivery.infrastructure.repository;

import com.logistics.delivery.domain.entity.Delivery;
import com.logistics.delivery.domain.entity.QDelivery;
import com.logistics.delivery.domain.entity.Status;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.logistics.delivery.domain.entity.QDelivery.delivery;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryCustomImpl implements DeliveryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Delivery> getDeliveries(String condition, String keyword, String status, Pageable pageable) {
        QDelivery delivery = QDelivery.delivery;

        BooleanExpression conditionExpression = buildConditionExpression(condition, keyword)
                .and(buildStatusExpression(status))
                .and(delivery.isDelete.ne(false));

        List<Delivery> result = queryFactory.selectFrom(delivery)
                .where(conditionExpression)
                .orderBy(getOrderSpecifier(pageable.getSort(), delivery))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = getTotalCount(delivery, conditionExpression);

        return new PageImpl<>(result, pageable, totalCount);
    }

    // 수령인, 출발허브, 도착허브, 배송지 기준의 검색어 조건
    public BooleanExpression buildConditionExpression(String condition, String keyword) {
        if ("recipientName".equals(condition)) {
            return delivery.recipientName.containsIgnoreCase(keyword);
        } else if ("sourceHubId".equals(condition)) {
            return delivery.sourceHubId.eq(UUID.fromString(keyword));
        } else if ("destinationHubId".equals(condition)) {
            return delivery.destinationHubId.eq(UUID.fromString(keyword));
        } else if ("address".equals(condition)) {
            return delivery.address.containsIgnoreCase(keyword);
        } else {
            return null;
        }
    }

    // 배송 상태 조건
    private BooleanExpression buildStatusExpression(String status) {
        if (status == null) {
            return null; // 조건 없음
        }

        try {
            Status parsedStatus = Status.valueOf(status.toUpperCase());
            return delivery.status.eq(parsedStatus);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private long getTotalCount(QDelivery delivery, BooleanExpression conditionExpression) {
        return queryFactory.selectFrom(delivery)
                .where(conditionExpression)
                .fetch().size();
    }

    // 정렬 조건
    private OrderSpecifier<?> getOrderSpecifier(Sort sort, QDelivery delivery) {
        boolean isAscending = sort.stream()
                .findFirst()
                .map(Sort.Order::isAscending)
                .orElse(false);

        return isAscending ? delivery.createdAt.asc() : delivery.createdAt.desc();
    }
}
