package com.logistics.product.application.service;

import com.logistics.product.application.dto.ProductReqDto;
import com.logistics.product.application.dto.ProductResDto;
import com.logistics.product.application.dto.ProductUpdateReqDto;
import com.logistics.product.domain.entity.Product;
import com.logistics.product.domain.repository.ProductRepository;
import com.logistics.product.infrastructure.feign.HubFeignClient;
import com.logistics.product.infrastructure.feign.VendorFeignClient;
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
    public UUID updateProduct(UUID productId, ProductUpdateReqDto request, String userId, String role) {

        Product product = productRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        // 사용자가 허브 담당자라면 hubId와 productId가 속한 hubId가 같은지 확인
        if(role.equals("HUB_Manager")) {
            UUID userHubId = hubFeignClient.getUserHubId(userId);
            if(userHubId.equals(product.getHubId())) {
                throw new IllegalArgumentException("해당 허브에 속한 상품이 아닙니다.");
            }
        }

        // 사용자가 업체 담당자라면 vendorId와 productId가 속한 vendorId가 같은지 확인
        if(role.equals("VENDOR_Manager")) {
            UUID userVendorId = vendorFeignClient.getUserVendorId(userId);
            if(userVendorId.equals(product.getVendorId())) {
                throw new IllegalArgumentException("해당 업체에 속한 상품이 아닙니다.");
            }
        }

        product.updateProduct(request.getName(), request.getStock(), request.getDescription(), request.getPrice());
        return productId;
    }

    @Transactional
    public void deleteProduct(UUID productId, String userId, String role) {

        Product product = productRepository.findByProductIdAndIsDeleteFalse(productId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        UUID userHubId = hubFeignClient.getUserHubId(userId);

        // 사용자가 허브 담당자라면 hubId와 productId가 속한 hubId가 같은지 확인
        if(role.equals("HUB_Manager")) {
           if(userHubId.equals(product.getHubId())) {
               throw new IllegalArgumentException("해당 허브에 속한 상품이 아닙니다.");
           }
        }
        product.softDelete(userId);
    }
}
