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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    @GetMapping("/{id}/list")
    public String getAllProduct(@PathVariable("id") int id,Model model){
        List<ProductDto> productList = productService.getAllProductByCatoryId(id);
        Category category = categoryService.findById(id);
        model.addAttribute("subCategory", category);
        model.addAttribute("products", productList);
        return "products";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable("id") int id, @RequestParam("subCategoryId") int subCategoryId, Model model){
        ProductDto productDto = productService.getProductById(id);
        Category category = categoryService.findById(subCategoryId);
        model.addAttribute("category", category);
        model.addAttribute("product", productDto);
        return "product-detail";
    }
}
