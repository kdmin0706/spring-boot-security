package com.springboot.security.controller;

import com.springboot.security.data.dto.ChangeProductNameDto;
import com.springboot.security.data.dto.ProductDto;
import com.springboot.security.data.dto.ProductResponseDto;
import com.springboot.security.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<ProductResponseDto> getProduct(Long number) {
        ProductResponseDto responseDto = this.productService.getProduct(number);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping()
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductDto productDto) {
        ProductResponseDto responseDto = this.productService.saveProduct(productDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping()
    public ResponseEntity<ProductResponseDto> changeProductName(
            @RequestBody ChangeProductNameDto changeProductNameDto) throws Exception {

        ProductResponseDto responseDto = this.productService.changeProductName(
                changeProductNameDto.getNumber(),
                changeProductNameDto.getName());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteProduct(Long number) throws Exception {
        this.productService.deleteProduct(number);

        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }
}
