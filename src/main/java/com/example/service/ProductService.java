package com.example.service;

import com.example.dto.ProductDto;
import com.example.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProductByCatoryId(int categoryId, int page, int size);
    ProductDto getProductById(int id);
    Integer findPriceByBrandAndAgeRange(String brand, String ageRange);
    List<Product> getAllProducts(int page, int size, String keyword);
    long countProducts(String keyword);
    List<Product> findByCategory0AndKeyword(int page, int size, String keyword);
    long countByCategory0AndKeyword(String keyword);
    Product getByName(String name);
    void saveOrUpdate(Product product);
    void deleteById(int id);
    Product findById(int id);
    List<Product> getMaxProducesBySix(int categoryId);
    long countByCategoryId(int categoryId);

}
