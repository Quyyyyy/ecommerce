package com.example.ecommerc.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private Double totalPrice;
    private String statusShipping;
    private LocalDate orderDate;
    private CouponDto couponDto;
    private PaymentDto paymentDto;
    private List<OrderItemDto> orderItemDtos;
}
