package com.example.ecommerc.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="categories")
public class Category extends BaseEntity{
    private String name;
    private String description;
    private String image;
//    @OneToMany(mappedBy = "category", cascade =  CascadeType.ALL )
//    private List<Product> products;
}
