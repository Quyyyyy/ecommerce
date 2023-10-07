package com.example.ecommerc.repository;

import com.example.ecommerc.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long>{
    Optional<Category> findByName(String name);
    @Query(value = "SELECT *\n" +
            "FROM categories c\n" +
            "where c.name like %:keyword% \n" +
            "or c.description like %:keyword%",nativeQuery = true)
    Page<Category> findCategoriesByKeyword(@Param("keyword") String keyword,
                                           Pageable pageable);
}
