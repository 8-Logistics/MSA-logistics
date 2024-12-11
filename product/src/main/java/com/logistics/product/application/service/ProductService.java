package com.logistics.product.application.service;

import com.logistics.product.application.dto.ProductReqDto;
import com.logistics.product.application.dto.ProductResDto;
import com.logistics.product.domain.entity.Product;
import com.logistics.product.domain.repository.ProductRepository;
import com.logistics.product.infrastucture.feign.HubFeignClient;
import com.logistics.product.infrastucture.feign.VendorFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VendorFeignClient vendorFeignClient;
    private final HubFeignClient hubFeignClient;
    @Transactional
    public ProductResDto createProduct(ProductReqDto request) {

        Product product = ProductReqDto.toProduct(request);

        if(!vendorFeignClient.checkVendor(request.getVendorId())) {
            throw new IllegalArgumentException("상품을 등록하려는 업체가 존재하지 않습니다.");
        }

        if(!hubFeignClient.checkHub(request.getHubId())){
            throw new IllegalArgumentException("상품을 등록하려는 업체의 소속 허브가 존재하지 않습니다.");
        }

        //product.setCreatedBy(userName);

        return ProductResDto.from(productRepository.save(product));
    }
}
