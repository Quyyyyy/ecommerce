package com.example.ecommerc.repository;

import com.example.ecommerc.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByTitle(String title);
    Boolean existsProductByTitle(String title);
    @Query(value = "select *\n" +
            "from products p\n" +
            "where p.category_id = :categoryId", nativeQuery = true)
    Page<Product> findProductByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    @Query(value = "SELECT *\n" +
            "FROM products p\n" +
            "where p.title like %:keyword% \n" +
            "or p.description like %:keyword%",nativeQuery = true)
    Page<Product> findProductsByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
