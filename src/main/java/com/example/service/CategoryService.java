package com.example.service;

import com.example.entity.Category;

import java.util.List;

public interface CategoryService {
    Category findById(int id);
    List<Category> getRootCategoriesWithSubs();
    List<Category> getCategoryWithSubs(Integer id);
}
