package com.example.ecommerc.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product_images")
public class ProductImage extends BaseEntity{
    private String sourceImage;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
}
