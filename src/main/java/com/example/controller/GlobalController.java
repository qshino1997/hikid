package com.example.controller;

import com.example.dto.Form.RegisterForm;
import com.example.dto.UserProfileDto;
import com.example.service.Cart.CartService;
import com.example.service.Cart.SessionCartService;
import com.example.util.CartUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalController {
    @Autowired
    private CartUtil cartUtils;

    @ModelAttribute
    public void addCartToModel(Model model, HttpSession session) {
        CartService cart = cartUtils.currentCart();
        model.addAttribute("cart", cart);
    }

    @ModelAttribute("cartCount")
    public Integer cartCount() {
        return cartUtils.currentCart().getTotalQuantity();
    }

    @ModelAttribute("registerForm")
    public RegisterForm registerForm() {
        return new RegisterForm(); // Khởi tạo mới mỗi lần, để form đăng ký luôn có modelAttribute này
    }
}
