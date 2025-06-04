package com.example.controller.Payment;

import com.example.dto.Form.OrderForm;
import com.example.entity.Order;
import com.example.service.Cart.CartService;
import com.example.service.OrderService;
import com.example.util.CartUtil;
import com.example.util.SecurityUtil;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/order")
public class PaymentOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartUtil cartUtil;

    @Autowired
    @Qualifier("databaseCartService")
    private CartService databaseCartService;

    /**
     * Bắt đầu thanh toán: tạo Order, gọi PayPal, redirect sang PayPal
     */
    @PostMapping("/checkout")
    public String checkout(@Valid @ModelAttribute("orderForm") OrderForm orderForm,
                           BindingResult br,
                           Model model) {

        // Nếu có lỗi validate, trả lại form kèm error messages
        if (br.hasErrors()) {
            model.addAttribute("orderForm", orderForm);
            model.addAttribute("cart", cartUtil.currentCart());
            return "cart";
        }

        if (cartUtil.currentCart() == null || cartUtil.currentCart().isEmpty()) {
            model.addAttribute("failed", "Giỏ hàng đang trống!");
            return "cart";
        }

        try {
            String approvalUrl = orderService.createPendingOrderAndGetApprovalUrl();
            return "redirect:" + approvalUrl;
        } catch (IllegalStateException | PayPalRESTException ex) {
            System.out.println("PayPal Checkout Error: " + ex.getMessage());
            return "cart";
        }
    }

    @GetMapping("/success")
    public String successPage(@RequestParam("paymentId") String paymentId,
                              @RequestParam("PayerID") String payerId,
                              Model model) {
        Order order = orderService.findByPaymentId(paymentId);
        if(order != null && !order.getIs_processed()){
            // === Clear giỏ hàng tại đây ===
            Integer userId = order.getUser_id();
            CartService dbCart = databaseCartService;

            dbCart.initForUser(userId);
            dbCart.clear();

            order.setIs_processed(true);
            orderService.saveOrUpdate(order);

            model.addAttribute("cartCount", 0);
            return "payment/success";
        } else{
            return "redirect:/";
        }
    }

    @GetMapping("/cancel")
    public String cancelPay(@RequestParam("token") String token) {
        Order order = orderService.findByToken(token);
        if (order != null && !"PAID".equals(order.getStatus())) {
            // Cập nhật order status = "CANCELED"
            order.setStatus("CANCELED");
            orderService.saveOrUpdate(order);
        }
        
        return "payment/failure";
    }
}
