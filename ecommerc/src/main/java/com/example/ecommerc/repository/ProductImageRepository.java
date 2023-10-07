package com.example.ecommerc.repository;

import com.example.ecommerc.entity.Product;
import com.example.ecommerc.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    @Query(value = "SELECT * \n" +
            "FROM product_images pi\n" +
            "where pi.product_id = :productId",nativeQuery = true)
    List<ProductImage> findProductsImagesByProductId(@Param("productId") Long productId);

    List<ProductImage> findProductImageByProduct(Product product);
    //Optional<ProductImage> findProductImageBy(Product product);
}
