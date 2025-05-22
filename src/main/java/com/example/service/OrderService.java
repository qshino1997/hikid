package com.example.service;

import com.example.entity.Order;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.http.HttpSession;

public interface OrderService {
    String createPendingOrderAndGetApprovalUrl(HttpSession session) throws PayPalRESTException;
    void handleWebhookEvent(String jsonPayload);
}
