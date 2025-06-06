package com.example.service.impl;

import com.example.dao.ProductReviewDAO;
import com.example.entity.ProductReview;
import com.example.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductReviewServiceImpl implements ProductReviewService {
    @Autowired
    private ProductReviewDAO productReviewDAO;

    @Override
    public List<ProductReview> findByProductId(int productId) {
        return productReviewDAO.findByProductId(productId);
    }

    @Override
    public long countByProductId(int productId) {
        return productReviewDAO.countByProductId(productId);
    }

    @Override
    public void saveOrUpdate(ProductReview review) {
        productReviewDAO.save(review);
    }

    @Override
    public void delete(int reviewId) {
        productReviewDAO.delete(reviewId);
    }

    @Override
    public ProductReview findByUserAndProduct(int userId, int productId) {
        return productReviewDAO.findByUserAndProduct(userId, productId);
    }
}
