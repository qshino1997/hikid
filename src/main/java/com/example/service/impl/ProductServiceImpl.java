package com.example.service.impl;

import com.example.dao.CategoryDAO;
import com.example.dao.ProductDAO;
import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public List<ProductDto> getAllProductByCatoryId(int id) {
        // 1. Lấy category
        Category category = categoryDAO.findById(id);

        // 2. Lấy luôn cả product của chính nó + của các subCategories (level 1)
        return Stream.concat(
                        // stream sản phẩm của chính category
                        productDAO.findAllByCatoryId(id).stream(),
                        // stream sản phẩm của từng subCategory (nếu có)
                        Optional.ofNullable(category.getSubCategories())
                                .orElse(Collections.emptyList())
                                .stream()
                                .flatMap(sub -> productDAO.findAllByCatoryId(sub.getId()).stream())
                )
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(int id){
        return productDAO.findById(id);
    }

    @Override
    public BigDecimal findPriceByBrandAndAgeRange(String brand, String ageRange) {
        return productDAO.findPriceByBrandAndAgeRange(brand,ageRange);
    }
}
