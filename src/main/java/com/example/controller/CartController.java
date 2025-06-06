package com.example.controller;

import com.example.dto.Form.OrderForm;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.service.Cart.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.util.CartUtil;
import com.example.util.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartUtil cartUtils;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ModelMapper modelMapper;

    // Thêm sản phẩm vào giỏ
    @PostMapping(value ="/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam("productId") Integer productId,
                                         @RequestParam("quantity") Integer quantity) {
        ProductDto p = productService.findById(productId);
        if (p != null) {
            cartUtils.currentCart().addItem(p, quantity);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("totalQuantity", cartUtils.currentCart().getTotalQuantity());

        return resp;
    }

    // Hiển thị giỏ hàng
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("orderForm", new OrderForm());

        CartService cartService = cartUtils.currentCart();
        if(securityUtil.getCurrentId() != null){
            User user = userService.findById(securityUtil.getCurrentId());
            OrderForm _orderForm = modelMapper.map(
                    user, OrderForm.class
            );
            _orderForm.setEmail_v2(user.getEmail());
            model.addAttribute("orderForm", _orderForm);
        }
        model.addAttribute("cart", cartService);

        return "cart";
    }

    // Cập nhật số lượng
    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCart(@RequestBody Map<String, Integer> payload) {
        Map<String, Object> result = new HashMap<>();
        Integer productId = payload.get("productId");
        Integer quantity = payload.get("quantity");

        if (productId == null || quantity == null || quantity < 1) {
            result.put("success", false);
            result.put("message", "Dữ liệu sản phẩm không hợp lệ.");
            return result;
        }

        cartUtils.currentCart().updateItem(productId, quantity);
        Integer totalQuantity = cartUtils.currentCart().getTotalQuantity();
        Long total = cartUtils.currentCart().getTotal();
        Product product = productService.getProductById(productId);
        BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());

        BigDecimal newSubtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

        result.put("newSubtotal", newSubtotal);
        result.put("success", true);
        result.put("totalQuantity", totalQuantity);
        result.put("totalCell", total);
        return result;

    }

    // Xóa 1 item
    @GetMapping("/remove/{productId}")
    public String removeItem(@PathVariable Integer productId,
                             HttpSession session) {
        cartUtils.currentCart().removeItem(productId);
        return "redirect:/cart";
    }
}
