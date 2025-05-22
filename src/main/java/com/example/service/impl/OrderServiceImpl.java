package com.example.service.impl;

import com.example.dao.OrderDao;
import com.example.entity.Order;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private APIContext apiContext;

    @Override
    public String createPendingOrderAndGetApprovalUrl(HttpSession session) throws PayPalRESTException {
        @SuppressWarnings("unchecked")
        CartService cartService = (CartService) session.getAttribute("CART");
        if (cartService == null || cartService.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng đang trống!");
        }

        // 1. Luu Order với PENDING_PAYMENT
        Order order = new Order();
        order.setUser_id(2);
        BigDecimal total = BigDecimal.valueOf(cartService.getTotal());
        order.setTotal_amount(total);
        order.setStatus("PENDING_PAYMENT");
        orderDao.save(order);

        // 2. Tạo PayPal Payment
        Payer payer = new Payer().setPaymentMethod("paypal");
        List<Item> items = cartService.getItems().stream()
                .map(ci -> new Item()
                        .setName(ci.getProduct().getName())
                        .setCurrency("USD")
                        .setPrice(ci.getProduct().getPrice().toString())
                       .setQuantity(String.valueOf(ci.getQuantity())))
                .collect(Collectors.toList());
        ItemList itemList = new ItemList().setItems(items);
        Amount amount = new Amount().setCurrency("USD").setTotal(order.getTotal_amount().toString());

        Transaction txn = (Transaction) new Transaction()
                .setAmount(amount)
                .setItemList(itemList)
                .setDescription("Thanh toán đơn hàng #" + order.getOrder_id())
                .setInvoiceNumber(order.getOrder_id().toString());

        RedirectUrls urls = new RedirectUrls()
                .setCancelUrl("https://059d-2405-4802-a5fe-1f50-959d-f90b-890-1b5e.ngrok-free.app/cart")
                .setReturnUrl("https://059d-2405-4802-a5fe-1f50-959d-f90b-890-1b5e.ngrok-free.app/order/success");

        Payment payment = new Payment()
                .setIntent("sale")
                .setPayer(payer)
                .setTransactions(Collections.singletonList(txn))
                .setRedirectUrls(urls);

        // 3. Gọi PayPal và xử lý response
        try {
            Payment created = payment.create(apiContext);
            String approvalUrl = created.getLinks().stream()
                    .filter(l -> "approval_url".equalsIgnoreCase(l.getRel()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Không tìm được approval_url"))
                    .getHref();

            // 4. Cập nhật paymentId vào order
            order.setPayment_id(created.getId());
            orderDao.save(order);

            return approvalUrl;

        } catch (PayPalRESTException ex) {
            // Ghi log, cập nhật trạng thái order
            order.setStatus("PAYMENT_FAILED");
            orderDao.save(order);
            throw new RuntimeException("Lỗi khi tạo PayPal payment: " + ex.getMessage(), ex);
        }

    }

    @Override
    public void handleWebhookEvent(String paymentId) {
        Order order = orderDao.findByPaymentId(paymentId);
        if (order != null) {
            // set status = “PAID”
            order.setStatus("PAID");
            orderDao.save(order);
            // Có thể gửi mail, log, push notification…
        } else {
            // Log: không tìm thấy order nào với paymentId này
            System.err.println("Webhook: Order not found for paymentId= " + paymentId);
        }
    }
}
