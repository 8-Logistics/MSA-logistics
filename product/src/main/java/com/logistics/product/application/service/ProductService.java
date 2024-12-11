package com.logistics.product.application.service;

import com.logistics.product.application.dto.ProductReqDto;
import com.logistics.product.application.dto.ProductResDto;
import com.logistics.product.application.dto.ProductUpdateReqDto;
import com.logistics.product.domain.entity.Product;
import com.logistics.product.domain.repository.ProductRepository;
import com.logistics.product.infrastucture.feign.HubFeignClient;
import com.logistics.product.infrastucture.feign.VendorFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VendorFeignClient vendorFeignClient;
    private final HubFeignClient hubFeignClient;
    @Transactional
    public UUID createProduct(ProductReqDto request) {

        Product product = ProductReqDto.toProduct(request);

        if(!vendorFeignClient.checkVendor(request.getVendorId())) {
            throw new IllegalArgumentException("상품을 등록하려는 업체가 존재하지 않습니다.");
        }

        if(!hubFeignClient.checkHub(request.getHubId())){
            throw new IllegalArgumentException("상품을 등록하려는 업체의 소속 허브가 존재하지 않습니다.");
        }

        //product.setCreatedBy(userName);

        ProductResDto.from(productRepository.save(product));
        return product.getProductId();
    }

    @Transactional
    public UUID updateProduct(UUID productId, ProductUpdateReqDto request) {
        Product product = productRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        product.updateProduct(request.getName(), request.getStock(), request.getDescription(), request.getPrice());
        return productId;
    }


}
