package com.example.controller;

import com.example.entity.*;
import com.example.service.OrderService;
import com.example.service.ProductReviewService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    // Hiển thị trang tình trạng đơn hàng của user đang đăng nhập
    @GetMapping("/history")
    public String orderStatus(Model model) {
        // Giả sử orderService trả về danh sách đơn hàng của user
        List<Order> orderList = orderService.findByUserId(securityUtil.getCurrentId());

        Map<Integer, ProductReview> reviewMap = new LinkedHashMap<>();
        for (Order o : orderList) {
            for (OrderItem item : o.getItems()) {
                int productId = item.getProduct().getProduct_id();
                ProductReview review = productReviewService.findByUserAndProduct(securityUtil.getCurrentId(), productId);
                if (review != null) {
                    reviewMap.put(productId, review);
                }
            }
        }
        model.addAttribute("reviewMap", reviewMap);
        model.addAttribute("orderList", orderList);
        return "order-history"; // dẫn đến /WEB-INF/views/orderStatus.jsp
    }

    @PostMapping("/items/{productId}/review")
    @ResponseBody
    public Map<String, Object> submitReviewAjax(
            @PathVariable("productId") Integer productId,
            @RequestBody Map<String, String> payload) {

        Map<String, Object> result = new HashMap<>();

        String ratingStr = payload.get("rating");
        if (ratingStr == null || ratingStr.isEmpty()) {
            result.put("success", false);
            result.put("message", "Bạn phải chọn số sao để đánh giá.");
            return result;
        }

        Integer rating;
        try {
            rating = Integer.valueOf(ratingStr);
            if (rating < 1 || rating > 5) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            result.put("success", false);
            result.put("message", "Giá trị rating không hợp lệ.");
            return result;
        }

        String comment = payload.get("comment");

        if(productId == null){
            result.put("success", false);
            result.put("message", "Mã số sản phẩm không có hoặc không hợp lệ");
            return result;
        }

        Product product = productService.getProductById(productId);
        User user = userService.findById(securityUtil.getCurrentId());

        ProductReview productReview = new ProductReview();
        productReview.setProduct(product);
        productReview.setUser(user);
        productReview.setRating(rating);
        productReview.setContent(comment);

        try {
            // Giả sử service lưu review cho từng product dựa vào user, rating, comment
            productReviewService.saveOrUpdate(productReview);
            result.put("success", true);
            result.put("comment", comment);
        } catch (Exception ex) {
            result.put("success", false);
            result.put("message", ex.getMessage());
        }
        return result;
    }

}
