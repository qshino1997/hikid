package com.example.controller;

import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{parentId}/children")
    @ResponseBody
    public List<Category> children(@PathVariable Integer parentId) {
        List<Category> category =  categoryService.getCategoryWithSubs(parentId);
        return categoryService.getCategoryWithSubs(parentId);
    }
}
