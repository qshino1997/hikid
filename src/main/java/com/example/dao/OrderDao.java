package com.example.dao;

import com.example.entity.Order;
import com.example.entity.OrderItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    void save(Order order);
    Order findByPaymentId(String paymentId);
    List<Order> findByDateRange(LocalDate startDate, LocalDate endDate, String keyword, int offset, int limit);
    long countOrders(LocalDate start, LocalDate end, String keyword);
    Map<LocalDate, Long> countOrdersByDate(LocalDate startDate, LocalDate endDate);
    List<OrderItem> findOrdersByOrderItemId(int orderId);
    List<Order> findByUserId(Integer userId);
    Order findByToken(String token);
}
