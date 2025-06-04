package com.example.controller;

import com.example.entity.Order;
import com.example.service.OrderService;
import com.example.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SecurityUtil securityUtil;
    // Hiển thị trang tình trạng đơn hàng của user đang đăng nhập
    @GetMapping("/history")
    public String orderStatus(Model model) {
        // Giả sử orderService trả về danh sách đơn hàng của user
        List<Order> orderList = orderService.findByUserId(securityUtil.getCurrentId());
        model.addAttribute("orderList", orderList);
        return "order-history"; // dẫn đến /WEB-INF/views/orderStatus.jsp
    }
}
