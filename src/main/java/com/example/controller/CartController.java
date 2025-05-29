package com.example.controller;

import com.example.dto.ProductDto;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    private CartService getCart(HttpSession session) {
        CartService cart = (CartService) session.getAttribute("CART");
        if (cart == null) {
            cart = new CartServiceImpl();
            session.setAttribute("CART", cart);
        }
        return cart;
    }

    // Thêm sản phẩm vào giỏ
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Integer productId,
                            @RequestParam("quantity") Integer quantity,
                            @RequestParam("mode") Integer mode,
                            @RequestParam(value = "categoryId", required = false) Integer categoryId,
                            RedirectAttributes ra,
                            HttpSession session) {
        ProductDto p = productService.findById(productId);
        if (p != null) {
            CartService cart = getCart(session);
            cart.addItem(p, quantity);
            if(mode == 1){
                ra.addFlashAttribute("success", "Thêm giỏ hàng thành công");
                return "redirect:/product/" + productId;
            } else if (mode == 2){
                ra.addFlashAttribute("success", "Thêm sản phẩm " + p.getName() +" vào giỏ hàng thành công");
                return "redirect:/";
            } else if (mode == 3){
                ra.addFlashAttribute("success", "Thêm sản phẩm " + p.getName() +" vào giỏ hàng thành công");
                return "redirect:/product/" + categoryId + "/list";
            }
        }
        // Chuyển về trang giỏ hàng
        return "redirect:/cart";
    }

    // Hiển thị giỏ hàng
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        CartService cart = getCart(session);
        model.addAttribute("cart", cart);
        return "cart";
    }

    // Cập nhật số lượng
    @PostMapping("/update")
    public String updateCart(@RequestParam("productId") Integer productId,
                             @RequestParam("quantity") Integer quantity,
                             HttpSession session) {
        CartService cart = getCart(session);
        cart.updateItem(productId, quantity);
        return "redirect:/cart";
    }

    // Xóa 1 item
    @GetMapping("/remove/{productId}")
    public String removeItem(@PathVariable Integer productId,
                             HttpSession session) {
        CartService cart = getCart(session);
        cart.removeItem(productId);
        return "redirect:/cart";
    }
}
