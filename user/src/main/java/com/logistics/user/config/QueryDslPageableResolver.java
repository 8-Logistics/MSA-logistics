package com.logistics.user.config;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;

public class QueryDslPageableResolver extends PageableHandlerMethodArgumentResolver {

    public static final List<Integer> ALLOWED_PAGE_SIZES = Arrays.asList(10, 20, 50);
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "createdAt");

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter,
                                    ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest,
                                    WebDataBinderFactory binderFactory) {
        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);


        return validatePageSize(pageable);
    }


    private Pageable validatePageSize(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        Sort sort = pageable.getSort();

        // 페이지 크기 검증 및 기본값 설정
        if (!QueryDslPageableResolver.ALLOWED_PAGE_SIZES.contains(pageSize)) {
            pageSize = QueryDslPageableResolver.DEFAULT_PAGE_SIZE;
        }

        // 정렬 검증 및 기본값 설정
        if (!sort.isSorted()) {
            sort = QueryDslPageableResolver.DEFAULT_SORT;
        }

        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
