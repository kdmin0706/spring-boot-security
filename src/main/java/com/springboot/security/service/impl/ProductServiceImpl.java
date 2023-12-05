package com.springboot.security.service.impl;

import com.springboot.security.data.dto.ProductDto;
import com.springboot.security.data.dto.ProductResponseDto;
import com.springboot.security.data.entity.Product;
import com.springboot.security.data.repository.ProductRepository;
import com.springboot.security.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    @Override
    public ProductResponseDto getProduct(Long number) {
        LOGGER.info("[getProduct] input number : {}", number);

        Product product = this.productRepository.findById(number).get();

        LOGGER.info("[getProduct] product number : {}, name : {}"
                , product.getNumber(), product.getName());

        return ProductResponseDto.builder()
                .number(product.getNumber())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    @Override
    public ProductResponseDto saveProduct(ProductDto productDto) {
        LOGGER.info("[saveProduct] ProductDto : {}", productDto.toString());

        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());

        Product savedProduct = this.productRepository.save(product);
        LOGGER.info("[saveProduct] savedProduct : {}", savedProduct);

        return ProductResponseDto.builder()
                .number(savedProduct.getNumber())
                .name(savedProduct.getName())
                .price(savedProduct.getPrice())
                .stock(savedProduct.getStock())
                .build();
    }

    @Override
    public ProductResponseDto changeProductName(Long number, String name) {
        Product foundProduct = this.productRepository.findById(number).get();
        foundProduct.setName(name);
        Product changedProduct = this.productRepository.save(foundProduct);

        return ProductResponseDto.builder()
                .number(changedProduct.getNumber())
                .name(changedProduct.getName())
                .price(changedProduct.getPrice())
                .stock(changedProduct.getStock())
                .build();
    }

    @Override
    public void deleteProduct(Long number) {
        this.productRepository.deleteById(number);
    }
}
