package com.example.service;

import com.example.dto.Form.CategoryOption;
import com.example.entity.Category;

import java.util.List;

public interface CategoryService {
    Category findById(int id);
    List<Category> getRootCategoriesWithSubs();
    List<Category> getCategoryWithSubs(Integer id);
    List<Category> getAllCategories(int page, int size, String keyword);
    long countCategories(String keyword);
    public List<CategoryOption> findParentsUpToLevel(int maxLevel);
    void save(Category category);
    void deleteById(Integer id);
    List<Category> getAll();
    Category findByName(String name);
}
