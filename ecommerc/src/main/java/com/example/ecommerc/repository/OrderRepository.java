package com.example.ecommerc.repository;

import com.example.ecommerc.entity.Order;
import com.example.ecommerc.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findOrderByUser(User user, Pageable pageable);
}
