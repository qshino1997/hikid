package com.example.dao;

import com.example.entity.Category;

import java.util.List;

public interface CategoryDAO {
    Category findById(int id);
    List<Category> findAllWithSubcategories();
    List<Category> findByIdWithSubcategories(Integer id);
    List<Category> findByKeyword(int page, int size, String keyword);
    long countByKeyword(String keyword);
    void saveOrUpdate(Category category);
    void deleteById(Integer id);
    List<Category> findAll();

    Category findByName(String name);
}
