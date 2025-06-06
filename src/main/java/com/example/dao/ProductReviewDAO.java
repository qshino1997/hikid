package com.example.dao;

import com.example.entity.ProductReview;

import java.util.List;

public interface ProductReviewDAO {
    List<ProductReview> findByProductId(int productId);
    long countByProductId(int productId);
    void save(ProductReview review);
    void delete(int reviewId);
    ProductReview findByUserAndProduct(int userId, int productId);

}
