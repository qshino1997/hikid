package com.example.service;

import com.example.entity.ProductReview;

import java.util.List;

public interface ProductReviewService {
    List<ProductReview> findByProductId(int productId);
    long countByProductId(int productId);
    void saveOrUpdate(ProductReview review);
    void delete(int reviewId);
    ProductReview findByUserAndProduct(int userId, int productId);
}
