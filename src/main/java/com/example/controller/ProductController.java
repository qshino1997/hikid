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
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}/list")
    public String getAllProduct(@PathVariable("categoryId") int categoryId,
                                Model model){
        Category category = categoryService.findById(categoryId);
        model.addAttribute("category", category);
        model.addAttribute("categoryId", category.getId());
        return "products";
    }

    @GetMapping("/searchList")
    public String getAllProductBySearch(
            @RequestParam(value="keyword", defaultValue="") String keyword,
            Model model){

        Category category = new Category();
        category.setName(keyword.trim());
        model.addAttribute("category", category);

        model.addAttribute("keyword", keyword);

        return "products-by-search";
    }

    @GetMapping("/{categoryId}/loadAjax")
    public String loadProductsAjax(
            @PathVariable(value = "categoryId") int categoryId,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="size", defaultValue="12") int size,
            @RequestParam(defaultValue="") String keyword,
            Model model) {

        Category category = categoryService.findById(categoryId);
        model.addAttribute("category", category);

        List<ProductDto> productList = productService.getAllProductByCatoryId(categoryId, page, size);

        long total = productService.countByCategoryId(categoryId);

        model.addAttribute("products", productList);
        model.addAttribute("page", page);
        model.addAttribute("pages", (int)Math.ceil((double)total/size));
        model.addAttribute("keyword", keyword);

        return "product-list";
    }

    @GetMapping("/searchLoadAjax")
    public String searchLoadProductsAjax(
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="size", defaultValue="12") int size,
            @RequestParam(defaultValue="") String keyword,
            Model model) {

        List<ProductDto> productList = productService.findByCategory0AndKeyword(page, size, keyword);
        long total = productService.countByCategory0AndKeyword(keyword);

        model.addAttribute("products", productList);
        model.addAttribute("page", page);
        model.addAttribute("pages", (int)Math.ceil((double)total/size));
        model.addAttribute("keyword", keyword);

        return "product-list";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable("id") int id, Model model){
        ProductDto product = productService.findById(id);

        int categoryId = product.getCategory_id();
        List<ProductDto> similarProducts = productService.findByCategory(categoryId);
        similarProducts.removeIf(p -> Objects.equals(p.getProduct_id(), product.getProduct_id()));

        model.addAttribute("product", product);
        model.addAttribute("similarProducts", similarProducts);


        return "product-detail";
    }
}
