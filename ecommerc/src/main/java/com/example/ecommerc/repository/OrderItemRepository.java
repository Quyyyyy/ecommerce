package com.example.ecommerc.repository;

import com.example.ecommerc.entity.Order;
import com.example.ecommerc.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findOrderItemByOrder(Order order);
}
