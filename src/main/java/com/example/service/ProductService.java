package com.example.service;

import com.example.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProductByCatoryId(int id);
    ProductDto getProductById(int id);
    BigDecimal findPriceByBrandAndAgeRange(String brand, String ageRange);
}
