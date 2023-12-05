package com.springboot.security.data.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private Long number;
    private String name;
    private int price;
    private int stock;
}
