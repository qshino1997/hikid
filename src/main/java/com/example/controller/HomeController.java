package com.example.controller;

import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
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
    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model){
        List<Category> categories = categoryService.getRootCategoriesWithSubs();
        List<ProductDto> milkList = productService.getMaxProducesBySix(3);
        List<ProductDto> vitaminList = productService.getMaxProducesBySix(4);
        List<ProductDto> toyList = productService.getMaxProducesBySix(1);
        List<ProductDto> clothesList = productService.getMaxProducesBySix(2);

        model.addAttribute("milkList", milkList);
        model.addAttribute("vitaminList", vitaminList);
        model.addAttribute("toyList", toyList);
        model.addAttribute("clothesList", clothesList);

        model.addAttribute("categories", categories);
        return "home";
    }




}
