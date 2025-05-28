package com.example.dao;

import com.example.dto.ProductDto;
import com.example.entity.Product;

import java.util.List;

public interface ProductDAO {
    List<Product> findAllByCatoryId(int categoryId, int page, int size);
    ProductDto findById(int id);
    Product findByName(String name);
    Integer findPriceByBrandAndAgeRange(String brand, String ageRange);
    List<Product> findByKeyword(int page, int size, String keyword);
    List<Product> findByCategory0AndKeyword(int page, int size, String keyword);
    long countByKeyword(String keyword);
    void save(Product product);
    void delete(int id);
    Product getById(int id);
    List<Product> getMaxProducesBySix(int categoryId);
    long countByCategoryId(int categoryId);
    long countByCategory0AndKeyword(String keyword);

}
