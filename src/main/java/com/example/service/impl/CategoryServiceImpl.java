package com.example.service.impl;

import com.example.dao.CategoryDAO;
import com.example.entity.Category;
import com.example.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;

    @Override
    public Category findById(int id) {
        return categoryDAO.findById(id);
    }

    @Override
    public List<Category> getRootCategoriesWithSubs() {
        List<Category> roots = categoryDAO.findAllWithSubcategories();
        // Load subCategories đệ quy trong cùng session
//        roots.forEach(cat -> {
//            cat.getSubCategories().forEach(sub -> {
//                sub.getSubCategories().size(); // ép Hibernate load luôn
//            });
//        });
        return roots;
    }

    @Override
    public List<Category> getCategoryWithSubs(Integer id) {
        return categoryDAO.findByIdWithSubcategories(id);
    }
}
