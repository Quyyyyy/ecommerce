package com.example.ecommerc.repository;

import com.example.ecommerc.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
    Boolean existsCouponByCode(String code);
}
