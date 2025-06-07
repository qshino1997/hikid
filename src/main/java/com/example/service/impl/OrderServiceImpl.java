package com.example.service.impl;

import com.example.config.Mail.MailService;
import com.example.dao.OrderDao;
import com.example.dto.CartItemDto;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.entity.Product;
import com.example.service.Cart.CartService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.util.CartUtil;
import com.example.util.SecurityUtil;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private APIContext apiContext;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProductService productService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartUtil cartUtil;
    @Override
    public String createPendingOrderAndGetApprovalUrl() throws PayPalRESTException {
        CartService cartService = cartUtil.currentCart();

        // Luu Order với PENDING_PAYMENT
        Order order = new Order();
        order.setUser_id(securityUtil.getCurrentId());
        BigDecimal total = BigDecimal.valueOf(cartService.getTotal());
        order.setTotal_amount(total);
        order.setStatus("PENDING_PAYMENT");

        Collection<CartItemDto> getItems = cartService.getItems();
        Set<OrderItem> orderItemList = new HashSet<>();
        // Tạo các OrderItem từ cartService.getItems()
        getItems.forEach(cartItemDto -> {
            OrderItem oi = new OrderItem();

            // Gán relation Order ←→ OrderItem
            oi.setOrder(order);

            // Lấy Product entity từ DB dựa trên productId từ CartItemDto
            Integer prodId = cartItemDto.getProduct().getProduct_id();
            Product product = productService.getProductById(prodId);
            oi.setProduct(product);

            // Gán số lượng và đơn giá
            oi.setQuantity(cartItemDto.getQuantity());
            oi.setUnit_price(BigDecimal.valueOf(cartItemDto.getProduct().getPrice()));

            // Thêm vào danh sách items của Order
            orderItemList.add(oi);
        });
        order.setItems(orderItemList);
        orderDao.save(order);

        // Tạo PayPal Payment
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
//                .setCancelUrl("https://hikid.onrender.com/cart")
//                .setReturnUrl("https://hikid.onrender.com/order/success");
                .setCancelUrl("https://7909-2405-4802-a31f-f80-7cad-ad87-37b8-8cf2.ngrok-free.app/order/cancel")
                .setReturnUrl("https://7909-2405-4802-a31f-f80-7cad-ad87-37b8-8cf2.ngrok-free.app/order/success");
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

            String token = null;
            int idx = approvalUrl.indexOf("token=");
            if (idx >= 0) {
                token = approvalUrl.substring(idx + 6);
                // Nếu URL còn các tham số khác phía sau token,
                // bạn có thể xử lý bằng cách cắt đến dấu & đầu tiên (nếu có):
                int ampIndex = token.indexOf('&');
                if (ampIndex > 0) {
                    token = token.substring(0, ampIndex);
                }
            }
            // 4. Cập nhật paymentId vào order
            order.setPayment_id(created.getId());
            order.setToken(token);
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
            sendConfirmationMail(order);
        } else {
            // Log: không tìm thấy order nào với paymentId này
            System.err.println("Webhook: Order not found for paymentId= " + paymentId);
        }
    }

    private void sendConfirmationMail(Order order) {
        String email = userService.findById(order.getUser_id()).getEmail();
        try {
            mailService.sendSimpleMail(
                    email,
                    "Xác nhận thanh toán",
                    "Đơn hàng #" + order.getPayment_id() + " của bạn đã được thanh toán. Chúng tôi sẽ giao hàng sớm nhất có thể."
            );
        } catch (MailException ex) {
            log.error("Gửi mail xác nhận thanh toán thất bại cho đơn {}: {}", order.getPayment_id(), ex.getMessage());
        }
    }

    @Override
    public List<Order> findOrders(LocalDate startDate, LocalDate endDate, String keyword, int page, int size) {
        return orderDao.findByDateRange(startDate, endDate, keyword, page, size);
    }

    @Override
    public long countOrders(LocalDate startDate, LocalDate endDate, String keyword) {
        return orderDao.countOrders(startDate, endDate, keyword);
    }

    @Override
    public Map<LocalDate, Long> countOrdersByDate(LocalDate startDate, LocalDate endDate) {
        return orderDao.countOrdersByDate(startDate, endDate);
    }

    @Override
    public List<OrderItem> findOrdersByOrderItemId(int orderId) {
        return orderDao.findOrdersByOrderItemId(orderId);
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        return orderDao.findByUserId(userId);
    }

    @Override
    public Order findByPaymentId(String paymentId) {
        return orderDao.findByPaymentId(paymentId);
    }

    @Override
    public void saveOrUpdate(Order order) {
        orderDao.save(order);
    }

    @Override
    public Order findByToken(String token) {
        return orderDao.findByToken(token);
    }
}
