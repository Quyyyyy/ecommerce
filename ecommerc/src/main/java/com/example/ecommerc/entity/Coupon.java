package com.example.ecommerc.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="coupons")
public class Coupon extends BaseEntity{
    private String code;
    private Double discount;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CouponType type;
    private Double conditionDiscount;
    private Date expirationDate;
    private int maxUsage;
}
