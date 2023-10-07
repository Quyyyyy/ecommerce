package com.example.ecommerc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto{
    private Long id;
    private String paymentMethod;
    private String provider;
    private String numberCard;
}
