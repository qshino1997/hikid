package com.example.dao;

import com.example.entity.Order;

public interface OrderDao {
    void save(Order order);
    Order findByPaymentId(String paymentId);
}
