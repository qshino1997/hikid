package com.example.controller;

import com.example.service.CartService;
import com.example.service.impl.CartServiceImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalController {
    @ModelAttribute
    public void addCartToModel(Model model, HttpSession session) {
        CartService cart = (CartService) session.getAttribute("CART");
        if (cart == null) {
            cart = new CartServiceImpl();
            session.setAttribute("CART", cart);
        }
        model.addAttribute("cart", cart);
    }
}
