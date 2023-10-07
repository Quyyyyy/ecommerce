package com.example.ecommerc.service;

import com.example.ecommerc.dto.CartItemDto;
import com.example.ecommerc.dto.PaymentDto;
import com.example.ecommerc.dto.Result;
import org.springframework.http.ResponseEntity;

public interface CartService {
    public ResponseEntity<Result> addToCart(Long productId,Integer quantity, String username);
    public ResponseEntity<Result> getCart(String username);
    public ResponseEntity<Result> removeItemInCart(String username, Long itemCartId);
    public ResponseEntity<Result> updateQuantityProduct(String username, CartItemDto cartItem, Long cartItemId);

}
