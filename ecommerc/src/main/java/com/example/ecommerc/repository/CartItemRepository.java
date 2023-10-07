package com.example.ecommerc.repository;

import com.example.ecommerc.entity.Cart;
import com.example.ecommerc.entity.CartItem;
import com.example.ecommerc.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByProductAndCart(Product product, Cart cart);

    List<CartItem> findCartItemByCart(Cart cart);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);
}
