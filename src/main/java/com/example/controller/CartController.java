package com.example.controller;

import com.example.dto.ProductDto;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

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
                            HttpSession session) {
        ProductDto p = productService.getProductById(productId);
        if (p != null) {
            CartService cart = getCart(session);
            cart.addItem(p, quantity);
        }
        // Chuyển về trang giỏ hàng (bạn map /gio-hang.html tới view "cart")
        return "redirect:/cart";
    }

    @PostMapping(path = "/addAjax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Object> addToCartAjax(@RequestParam("product_id") Integer productId,
                                            @RequestParam("quantity")  Integer quantity,
                                            HttpSession session) {
        ProductDto p = productService.getProductById(productId);
        if (p != null) {
            CartService cart = getCart(session);
            cart.addItem(p, quantity);
        }
        // Trả về tổng số lượng sau khi thêm
        int totalQty = getCart(session).getTotalQuantity();
        return Collections.singletonMap("totalQuantity", totalQty);
    }

    // Hiển thị giỏ hàng
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        CartService cart = getCart(session);
        model.addAttribute("cart", cart);
        return "cart";  // tương ứng /WEB-INF/views/cart.jsp
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
