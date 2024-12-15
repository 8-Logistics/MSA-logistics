package com.logistics.vendor.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.logistics.vendor.application.dto.VendorReadResDTO;
import com.logistics.vendor.domain.entity.QVendor;
import com.logistics.vendor.domain.enums.SortOption;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VendorRepositoryImpl implements VendorRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;
	QVendor vendor = QVendor.vendor;

	@Override
	public Page<VendorReadResDTO> searchVendors(
		UUID vendorId, Pageable pageable, String keyword, SortOption sortOption) {

		List<VendorReadResDTO> vendorResponses = jpaQueryFactory
			.select(
				Projections.constructor(
					VendorReadResDTO.class,
					vendor.id,
					vendor.name,
					vendor.vendorType,
					vendor.sourceHubId,
					vendor.address,
					vendor.managerId,
					vendor.updatedAt
				))
			.from(vendor)
			.where(
				keywordContains(keyword),
				(vendorId != null ? vendor.id.eq(vendorId) : null)
			)
			.orderBy(getOrderSpecifier(sortOption, vendor))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(vendor.count())
			.from(vendor)
			.where(
				keywordContains(keyword),
				(vendorId != null ? vendor.id.eq(vendorId) : null)
			)
			.fetchOne();

		return new PageImpl<>(vendorResponses, pageable, total != null ? total : 0L);
	}

	private BooleanExpression keywordContains(String keyword) {
		if (keyword != null && !keyword.isEmpty()) {
			String modifiedKeyword = "%" + keyword + "%";
			return vendor.name.likeIgnoreCase(modifiedKeyword).or(vendor.address.likeIgnoreCase(modifiedKeyword));
		}
		return null;
	}

	private OrderSpecifier<?> getOrderSpecifier(SortOption sortOption, QVendor vendor) {
		return switch (sortOption) {
			case CREATED_AT_ASC -> vendor.createdAt.asc();
			case CREATED_AT_DESC -> vendor.createdAt.desc();
			case UPDATED_AT_ASC -> vendor.updatedAt.asc();
			case UPDATED_AT_DESC -> vendor.updatedAt.desc();
			case NAME_DESC -> vendor.name.desc();

			default -> vendor.name.asc();
		};
	}

}
