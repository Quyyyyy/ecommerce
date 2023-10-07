package com.example.ecommerc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="carts")
public class Cart extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
//    @OneToMany(mappedBy = "cart", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER,orphanRemoval = true)
//    private List<CartItem> cartItems = new ArrayList<>();
}
