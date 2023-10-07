package com.example.ecommerc.service;

import com.example.ecommerc.dto.CouponDto;
import com.example.ecommerc.dto.Result;
import org.springframework.http.ResponseEntity;

public interface CouponService {
    ResponseEntity<Result> getAllCoupon(int page, int size);
    ResponseEntity<Result> addCoupon(CouponDto couponDto);
    ResponseEntity<Result> updateCoupon(CouponDto couponDto, Long couponId);
    ResponseEntity<Result> deleteCoupon(Long couponId);
    ResponseEntity<Result> activeCoupon(Long couponId);
}
