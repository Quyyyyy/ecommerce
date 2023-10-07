package com.example.ecommerc.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private Integer quantity;
    private CategoryDto categoryDto;
    private List<ProductImageDto> imageDtos = new ArrayList<>();
}
