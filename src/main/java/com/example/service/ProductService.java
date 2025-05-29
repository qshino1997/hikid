package com.example.service;

import com.example.dto.ProductDto;
import com.example.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProductByCatoryId(int categoryId, int page, int size);
    Product getProductById(int id);
    Integer findPriceByBrandAndAgeRange(String brand, String ageRange);
    List<ProductDto> getAllProducts(int page, int size, String keyword);
    long countProducts(String keyword);
    List<ProductDto> findByCategory0AndKeyword(int page, int size, String keyword);
    long countByCategory0AndKeyword(String keyword);
    Product getByName(String name);
    void saveOrUpdate(Product product);
    void deleteById(int id);
    ProductDto findById(int id);
    List<ProductDto> getMaxProducesBySix(int categoryId);
    long countByCategoryId(int categoryId);

}
