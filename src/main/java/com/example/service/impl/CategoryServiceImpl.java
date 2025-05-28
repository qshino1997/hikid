package com.example.service.impl;

import com.example.dao.CategoryDAO;
import com.example.dto.CategoryOption;
import com.example.entity.Category;
import com.example.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
        return categoryDAO.findAllWithSubcategories();
    }

    @Override
    public List<Category> getCategoryWithSubs(Integer id) {
        return categoryDAO.findByIdWithSubcategories(id);
    }

    @Override
    public List<Category> getAllCategories(int page, int size, String keyword) {
        return categoryDAO.findByKeyword(page, size, keyword);
    }

    @Override
    public List<Category> getAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Category findByName(String name) {
        return categoryDAO.findByName(name);
    }

    @Override
    public long countCategories(String keyword) {
        return categoryDAO.countByKeyword(keyword);
    }

    @Override
    public List<CategoryOption> findParentsUpToLevel(int maxLevel) {
        List<Category> roots = categoryDAO.findAllWithSubcategories();  // level=1
        List<CategoryOption> result = new ArrayList<>();
        buildOptions(roots, 1, maxLevel, result);
        return result;
    }

    @Override
    public void save(Category category) {
        categoryDAO.saveOrUpdate(category);
    }

    @Override
    public void deleteById(Integer id) {
        categoryDAO.deleteById(id);
    }

    private void buildOptions(List<Category> nodes,
                              int level,
                              int maxLevel,
                              List<CategoryOption> result) {
        String prefix = String.join("",
                Collections.nCopies(level - 1, "\u00A0\u00A0\u00A0"));
        for (Category c : nodes) {
            // add current node
            result.add(new CategoryOption(c.getId(), prefix + c.getName()));
            // nếu chưa vượt quá maxLevel thì đệ quy con
            if (level < maxLevel) {
                buildOptions(
                        // đảm bảo subCategories đã được JOIN FETCH
                        new ArrayList<>(c.getSubCategories()),
                        level + 1,
                        maxLevel,
                        result
                );
            }
        }
    }
}
