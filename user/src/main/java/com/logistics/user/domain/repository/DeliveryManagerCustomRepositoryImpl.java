package com.logistics.user.domain.repository;

import com.logistics.user.application.dto.DeliveryManagerSearchReqDto;
import com.logistics.user.application.dto.DeliveryManagerSearchResDto;
import com.logistics.user.domain.entity.QDeliveryManager;
import com.logistics.user.domain.enums.DeliveryManagerType;
import com.logistics.user.domain.enums.DeliveryStatus;
import com.logistics.user.infrastructure.repository.DeliveryManagerCustomRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DeliveryManagerCustomRepositoryImpl implements DeliveryManagerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;

    @Override
    public Page<DeliveryManagerSearchResDto> getDeliveryManagerSearch(
            DeliveryManagerSearchReqDto deliveryManagerSearchReqDto, Pageable pageable, String username, String role) {

        List<DeliveryManagerSearchResDto> results = jpaQueryFactory
                .select(
                        Projections.constructor(
                                DeliveryManagerSearchResDto.class,
                                deliveryManager.id,
                                deliveryManager.deliveryManagerType,
                                deliveryManager.sourceHubId,
                                deliveryManager.deliverySequence,
                                deliveryManager.deliveryStatus
                        )
                ).from(deliveryManager)
                .where(
                        checkHubId(deliveryManagerSearchReqDto.getHubIdList()),
                        checkDeliveryManagerType(deliveryManagerSearchReqDto.getDeliveryManagerType()),
                        checkDeliveryStatus(deliveryManagerSearchReqDto.getDeliveryStatus()),
                        sequenceBetween(deliveryManagerSearchReqDto.getMaxSequence(), deliveryManagerSearchReqDto.getMinSequence())
                )
                .orderBy(getOrderSpecifiers(pageable).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(deliveryManager.count())
                .from(deliveryManager)
                .where(
                        checkHubId(deliveryManagerSearchReqDto.getHubIdList()),
                        checkDeliveryManagerType(deliveryManagerSearchReqDto.getDeliveryManagerType()),
                        checkDeliveryStatus(deliveryManagerSearchReqDto.getDeliveryStatus()),
                        sequenceBetween(deliveryManagerSearchReqDto.getMaxSequence(), deliveryManagerSearchReqDto.getMinSequence())
                )
                .fetchOne()).orElse(0L);


        return new PageImpl<>(results, pageable, total);
    }

    // 허브 id로 검색 조건
    private BooleanExpression checkHubId(List<UUID> hubIdList) {
        return !hubIdList.isEmpty() ? deliveryManager.sourceHubId.in(hubIdList) : null;
    }

    // 배송 담당자 타입으로 검색
    private BooleanExpression checkDeliveryManagerType(DeliveryManagerType deliveryManagerType) {
        return deliveryManagerType != null ? deliveryManager.deliveryManagerType.eq(deliveryManagerType) : null;
    }

    // 배송 상태 status로 검색
    private BooleanExpression checkDeliveryStatus(DeliveryStatus deliveryStatus) {
        return deliveryStatus != null ? deliveryManager.deliveryStatus.eq(deliveryStatus) : null;
    }

    private BooleanExpression sequenceBetween(Long maxSequence, Long minSequence) {
        if (maxSequence != null && minSequence != null) {
            return deliveryManager.deliverySequence.between(minSequence, maxSequence); // min이상 max 이하 값 return
        } else if (minSequence != null) {
            return deliveryManager.deliverySequence.goe(minSequence); // minSequence 이상인 값
        } else if (maxSequence != null) {
            return deliveryManager.deliverySequence.loe(maxSequence); // maxSequence 이상인 값
        } else {
            return null;
        }
    }

    // Java 12 이상에서 사용할 수 있는 "람다 스타일"의 switch 표현식을 활용
    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        Sort sort = pageable.getSort();
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : sort) {
            String field = order.getProperty();
            Sort.Direction direction = order.getDirection();

            // switch 표현식을 사용하여 정렬 필드를 처리
            switch (field) {
                case "createdAt" -> {
                    if (direction == Sort.Direction.ASC) {
                        orderSpecifiers.add(deliveryManager.createdAt.asc());
                    } else {
                        orderSpecifiers.add(deliveryManager.createdAt.desc());
                    }
                }
                case "updatedAt" -> {
                    if (direction == Sort.Direction.ASC) {
                        orderSpecifiers.add(deliveryManager.updatedAt.asc());
                    } else {
                        orderSpecifiers.add(deliveryManager.updatedAt.desc());
                    }
                }
                case "deliverySequence" -> {
                    if (direction == Sort.Direction.ASC) {
                        orderSpecifiers.add(deliveryManager.deliverySequence.asc());
                    } else {
                        orderSpecifiers.add(deliveryManager.deliverySequence.desc());
                    }
                }
                default -> {
                    // 기본적으로 createdAt으로 정렬 (DESC)
                    orderSpecifiers.add(deliveryManager.createdAt.desc());
                }
            }
        }

        return orderSpecifiers;
    }
}
