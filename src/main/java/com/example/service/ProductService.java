package com.example.service;

import com.example.dto.ProductDto;
import com.example.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ProductService {
    List<ProductDto> getAllProductByCatoryId(int id);
    ProductDto getProductById(int id);
    BigDecimal findPriceByBrandAndAgeRange(String brand, String ageRange);
}
