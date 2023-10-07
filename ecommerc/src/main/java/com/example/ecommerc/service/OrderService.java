package com.example.ecommerc.service;

import com.example.ecommerc.dto.CartDto;
import com.example.ecommerc.dto.OrderDto;
import com.example.ecommerc.dto.Result;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    public ResponseEntity<Result> getAllUserOrder(String username, int page, int size);
    public ResponseEntity<Result> getAllOrder(int page, int size);
    public ResponseEntity<Result> orderProduct(String username,OrderDto orderDto);
    public ResponseEntity<Result> getOrderDetail( Long orderId);
}
