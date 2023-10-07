package com.example.ecommerc.service;

import com.example.ecommerc.entity.Order;
import com.example.ecommerc.entity.User;

public interface EmailService {
    void sendMail(Order order, User user);
}
