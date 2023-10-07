package com.example.ecommerc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class Order extends BaseEntity{
    private String name;
    private String email;
    private String address;
    private String phone;
    private Double totalPrice;
    private String statusShipping;
    private LocalDate orderDate;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
//    @OneToMany(mappedBy = "order", cascade = { CascadeType.ALL })
//    private List<OrderItem> orderItems = new ArrayList<>();
}
