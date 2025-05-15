package com.example.controller;

import com.example.entity.Category;
import com.example.entity.Product;
import com.example.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model){
        List<Category> categories = categoryService.getRootCategoriesWithSubs();
        model.addAttribute("categories", categories);
        return "home";
    }


}
