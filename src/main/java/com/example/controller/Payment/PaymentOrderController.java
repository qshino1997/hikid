package com.example.controller.Payment;

import com.example.service.CartService;
import com.example.service.OrderService;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class PaymentOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Bắt đầu thanh toán: tạo Order, gọi PayPal, redirect sang PayPal
     */
    @GetMapping("/checkout")
    public String checkout(HttpSession session) {
        try {
            String approvalUrl = orderService.createPendingOrderAndGetApprovalUrl(session);
            return "redirect:" + approvalUrl;
        } catch (IllegalStateException | PayPalRESTException ex) {
            session.setAttribute("ERROR_MSG", ex.getMessage());
            System.out.println("PayPal Checkout Error: " + ex.getMessage());
            return "redirect:/cart";
        }
    }

    /**
     * Callback từ PayPal khi người dùng thanh toán thành công
     * Chỉ dùng để hiển thị trang cảm ơn, không cập nhật trạng thái đơn hàng ở đây
     */
    @GetMapping("/success")
    public String successPage(@RequestParam("paymentId") String paymentId,
                              @RequestParam("PayerID") String payerId,
                              Model model,
                              HttpSession session) {
        // TODO: Hiển thị thông báo cảm ơn + đơn hàng đã tiếp nhận
        model.addAttribute("message", "Cảm ơn bạn! Thanh toán đã được xử lý. Đơn hàng sẽ sớm được giao.");

        // === Clear giỏ hàng tại đây ===
        CartService cartService = (CartService) session.getAttribute("CART");
        if (cartService != null) {
            cartService.clear();
            session.removeAttribute("CART");   // tuỳ bạn có muốn remove luôn session attribute hay không
        }

        return "payment/success";
    }

    /**
     * Callback từ PayPal khi thanh toán bị hủy
     */
    @GetMapping("/cancel")
    public String cancelPay(Model model) {
        model.addAttribute("message", "Thanh toán đã bị hủy.");
        return "payment/failure";
    }
}
