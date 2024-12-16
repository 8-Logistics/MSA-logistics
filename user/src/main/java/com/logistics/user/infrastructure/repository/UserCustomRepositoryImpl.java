package com.logistics.user.infrastructure.repository;

import com.logistics.user.application.dto.UserSearchReqDto;
import com.logistics.user.application.dto.UserSearchResDto;
import com.logistics.user.domain.entity.QUser;
import com.logistics.user.domain.enums.UserRole;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QUser user = QUser.user;

    @Override
    public Page<UserSearchResDto> searchUsers(UserSearchReqDto searchRequest, Pageable pageable) {

        List<UserSearchResDto> results = jpaQueryFactory
                .select(
                        Projections.constructor(
                                UserSearchResDto.class,
                                user.id,
                                user.username,
                                user.email,
                                user.slackId,
                                user.name,
                                user.role
                        )
                ).from(user)
                .where(
                        checkUserId(searchRequest.getUserIdList()),
                        containsKeyword(searchRequest.getKeyword()),
                        checkIsDelete(searchRequest.getIsDelete()),
                        checkRole(searchRequest.getRole())
                )
                .orderBy(getOrderSpecifiers(pageable).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(jpaQueryFactory
                .select(user.count())
                .from(user)
                .where(
                        checkUserId(searchRequest.getUserIdList()),
                        containsKeyword(searchRequest.getKeyword()),
                        checkIsDelete(searchRequest.getIsDelete()),
                        checkRole(searchRequest.getRole())
                )
                .fetchOne()).orElse(0L);


        return new PageImpl<>(results, pageable, total);

    }


    // 값이 있는 userId 데이터 가져오기
    private BooleanExpression checkUserId(List<Long> userIdList) {
        return !CollectionUtils.isEmpty(userIdList)? user.id.in(userIdList) : null;
    }

    // 로그인 Id, email, slackId, name으로 검색
    private BooleanExpression containsKeyword(String keyword) {

        if(keyword != null && !keyword.isEmpty()){
            return user.username.containsIgnoreCase(keyword)
                    .or(user.email.containsIgnoreCase(keyword))
                    .or(user.slackId.containsIgnoreCase(keyword))
                    .or(user.name.containsIgnoreCase(keyword));
        }

        return null;

    }

    // true일때 삭제된 사람만 조회
    private BooleanExpression checkIsDelete(Boolean isDelete) {

        if(isDelete != null){
            return isDelete ? user.isDelete.isTrue() : user.isDelete.isFalse();
        }

        return null;
    }

    // 권한 별로 조회
    private BooleanExpression checkRole(UserRole role) {
        return role != null ? user.role.eq(role) : null;
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
                        orderSpecifiers.add(user.createdAt.asc());
                    } else {
                        orderSpecifiers.add(user.createdAt.desc());
                    }
                }
                case "updatedAt" -> {
                    if (direction == Sort.Direction.ASC) {
                        orderSpecifiers.add(user.updatedAt.asc());
                    } else {
                        orderSpecifiers.add(user.updatedAt.desc());
                    }
                }
                default -> {
                    // 기본적으로 createdAt으로 정렬 (DESC)
                    orderSpecifiers.add(user.createdAt.desc());
                }
            }
        }

        return orderSpecifiers;
    }

}
