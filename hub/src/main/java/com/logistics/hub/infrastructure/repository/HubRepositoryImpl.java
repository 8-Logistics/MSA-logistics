package com.logistics.hub.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.logistics.hub.application.dto.HubReadResDto;
import com.logistics.hub.domain.entity.QHub;
import com.logistics.hub.domain.enums.SortOption;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	QHub hub = QHub.hub;

	@Override
	public Page<HubReadResDto> searchHubs(UUID hubId, Pageable pageable, String keyword, SortOption sortOption) {

		List<HubReadResDto> hubResponses = jpaQueryFactory
			.select(
				Projections.constructor(
					HubReadResDto.class,
					hub.id,
					hub.name,
					hub.address,
					hub.managerId,
					hub.createdAt
				))
			.from(hub)
			.where(
				keywordContains(keyword)
					.and(hubId != null ? hub.id.eq(hubId) : null)
			)
			.orderBy(getOrderSpecifier(sortOption, hub))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(hub.count())
			.from(hub)
			.where(
				keywordContains(keyword)
					.and(hubId != null ? hub.id.eq(hubId) : null)
			)
			.fetchOne();
		return new PageImpl<>(hubResponses, pageable, total != null ? total : 0L);
	}

	private BooleanExpression keywordContains(String keyword) {
		if (keyword != null && !keyword.isEmpty()) {
			String modifiedKeyword = "%" + keyword + "%";
			return hub.name.likeIgnoreCase(modifiedKeyword).or(hub.address.likeIgnoreCase(modifiedKeyword));
		}
		return null;
	}

	private OrderSpecifier<?> getOrderSpecifier(SortOption sortOption, QHub hub) {
		return switch (sortOption) {
			case CREATED_AT_ASC -> hub.createdAt.asc();
			case CREATED_AT_DESC -> hub.createdAt.desc();
			case UPDATED_AT_ASC -> hub.updatedAt.asc();
			case UPDATED_AT_DESC -> hub.updatedAt.desc();
			case NAME_DESC -> hub.name.desc();

			default -> hub.name.asc();
		};
	}

}
