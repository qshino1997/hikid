package com.example.dao;

import com.example.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDAO {
    List<ProductDto> findAllByCatoryId(int id);
    ProductDto findById(int id);

    BigDecimal findPriceByBrandAndAgeRange(String brand, String ageRange);
}
