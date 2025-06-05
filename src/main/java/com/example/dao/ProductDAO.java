package com.example.dao;

import com.example.dto.ProductDto;
import com.example.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    List<ProductDto> findAllByCatoryId(int categoryId, int page, int size);
    Product findById(int id);
    Product findByName(String name);
    List<ProductDto> findByKeyword(int page, int size, String keyword);
    List<ProductDto> findByCategory0AndKeyword(int page, int size, String keyword);
    long countByKeyword(String keyword);
    void save(Product product);
    void delete(int id);
    ProductDto getById(int id);
    List<ProductDto> getMaxProducesBySix(int categoryId);
    long countByCategoryId(int categoryId);
    long countByCategory0AndKeyword(String keyword);
    List<ProductDto> findByCategoryId(int categoryId);
}
