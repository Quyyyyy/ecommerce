package com.example.ecommerc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Product extends BaseEntity{
    private String title;
    private Double price;
    private String description;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
//    @OneToMany(mappedBy = "product", cascade =  CascadeType.ALL )
//    private List<ProductImage> productImages = new ArrayList<>();
}
