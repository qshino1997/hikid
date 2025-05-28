package com.example.controller.Admin;

import com.example.dto.OrderStatsDto;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.entity.User;
import com.example.service.OrderService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Trang chính
    @GetMapping
    public String viewPage(Model model) {
        LocalDate today = LocalDate.now();
        LocalDate firstOfMonth = today.withDayOfMonth(1);

        // format theo yyyy-MM-dd
        model.addAttribute("startDateDefault", firstOfMonth);
        model.addAttribute("endDateDefault", today);

        return "admin.order";
    }

    @GetMapping("/ajax")
    public String loadAjax(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value="keyword", required=false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        List<Order> orders = orderService.findOrders(startDate, endDate, keyword, page, size);
        long totalItems = orderService.countOrders(startDate, endDate, keyword);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Map<Integer, String> formattedDates = orders.stream()
                .collect(Collectors.toMap(
                        Order::getOrder_id,
                        o -> o.getUpdated_at().format(fmt)
                ));

        model.addAttribute("orders", orders);
        model.addAttribute("formattedDates", formattedDates);
        model.addAttribute("page", page);
        model.addAttribute("pages", totalPages);
        model.addAttribute("keywordDefault", keyword);


        return "admin/order-table";
    }

    @GetMapping("/stats")
    @ResponseBody
    public List<OrderStatsDto> getOrderStats(
            @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate) {

        Map<LocalDate, Long> map = orderService.countOrdersByDate(startDate, endDate);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return map.entrySet().stream()
                .map(e -> {
                    OrderStatsDto s = new OrderStatsDto();
                    s.setDate(e.getKey().format(fmt));
                    s.setCount(e.getValue());
                    return s;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{orderId}/{userId}/details")
    public String getOrderDetails(@PathVariable("orderId") int orderId,
                                  @PathVariable("userId") int userId,
                                  Model model) {
        // Lấy đơn hàng
        List<OrderItem> orderItem = orderService.findOrdersByOrderItemId(orderId);

        // Lấy user từ đơn hàng
        User user = userService.findByIdAndProfile(userId);

        model.addAttribute("user", user);
        model.addAttribute("orderItem", orderItem);

        return "admin/order-details";
    }
}
