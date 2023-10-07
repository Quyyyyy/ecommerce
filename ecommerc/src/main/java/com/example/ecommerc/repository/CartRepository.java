package com.example.ecommerc.repository;

import com.example.ecommerc.entity.Cart;
import com.example.ecommerc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findCartByUser(User user);
}
