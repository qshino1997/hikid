package com.example.dao;

import com.example.entity.Category;

import java.util.List;

public interface CategoryDAO {
    Category findById(int id);
    List<Category> findAllWithSubcategories();
    List<Category> findByIdWithSubcategories(Integer id);
}
