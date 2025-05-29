package com.example.service.impl;

import com.example.dao.CategoryDAO;
import com.example.dao.ProductDAO;
import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    @Override
    public List<ProductDto> getAllProductByCatoryId(int categoryId, int page, int size) {
        // 1. Lấy category
        Category category = categoryDAO.findById(categoryId);

        // 2. Lấy luôn cả product của chính nó + của các subCategories (level 1)
        return Stream.concat(
                        // stream sản phẩm của chính category
                        productDAO.findAllByCatoryId(categoryId, page, size).stream(),
                        // stream sản phẩm của từng subCategory (nếu có)
                        Optional.ofNullable(category.getSubCategories())
                                .orElse(Collections.emptyList())
                                .stream()
                                .flatMap(sub -> productDAO.findAllByCatoryId(sub.getId(), page, size).stream())
                )
                .collect(Collectors.toList());
    }

    @Override
    public long countByCategoryId(int categoryId){
        // 1. Lấy category chính
        Category category = categoryDAO.findById(categoryId);

        // 2. Count của chính category
        long total = productDAO.countByCategoryId(categoryId);

        // 3. Count của từng sub-category (level 1)
        if (category.getSubCategories() != null) {
            for (Category sub : category.getSubCategories()) {
                total += productDAO.countByCategoryId(sub.getId());
            }
        }

        return total;
    }


    @Override
    public Product getProductById(int id){
        return productDAO.findById(id);
    }

    @Override
    public Integer findPriceByBrandAndAgeRange(String brand, String ageRange) {
        return productDAO.findPriceByBrandAndAgeRange(brand,ageRange);
    }

    @Override
    public List<ProductDto> getAllProducts(int page, int size, String keyword) {
        return productDAO.findByKeyword(page, size, keyword);
    }

    @Override
    public long countProducts(String keyword) {
        return productDAO.countByKeyword(keyword);
    }

    @Override
    public List<ProductDto> findByCategory0AndKeyword(int page, int size, String keyword) {
        return productDAO.findByCategory0AndKeyword(page, size, keyword);
    }

    @Override
    public long countByCategory0AndKeyword(String keyword) {
        return productDAO.countByCategory0AndKeyword(keyword);
    }

    @Override
    public Product getByName(String name){
        return productDAO.findByName(name);
    }

    @Override
    public void saveOrUpdate(Product product){
        productDAO.save(product);
    }

    @Override
    public void deleteById(int id) {
        productDAO.delete(id);
    }

    @Override
    public ProductDto findById(int id){
        return productDAO.getById(id);
    }

    @Override
    public List<ProductDto> getMaxProducesBySix(int categoryId) {
        return productDAO.getMaxProducesBySix(categoryId);
    }
}
