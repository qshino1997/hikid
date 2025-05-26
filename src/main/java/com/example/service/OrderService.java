package com.example.service;

import com.example.dto.PageResult;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {
    String createPendingOrderAndGetApprovalUrl(HttpSession session) throws PayPalRESTException;
    void handleWebhookEvent(String jsonPayload);
    List<Order> findOrders(LocalDate startDate, LocalDate endDate, String keyword, int page, int size);
    long countOrders(LocalDate startDate, LocalDate endDate, String keyword);
    Map<LocalDate, Long> countOrdersByDate(LocalDate startDate, LocalDate endDate);
    List<OrderItem> findOrdersByOrderItemId(int orderId);
}
