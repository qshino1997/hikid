package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.ProductReviewDAO;
import com.example.entity.ProductReview;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductReviewDAOImpl extends BaseDAO implements ProductReviewDAO {
    @Override
    public List<ProductReview> findByProductId(int productId) {
        // Trả về tất cả review của productId, sắp xếp theo createdAt giảm dần
        String hql = "FROM ProductReview pr " +
                "LEFT JOIN FETCH pr.user u " +
                "WHERE pr.product.product_id = :pid " +
                "ORDER BY pr.created_at DESC";
        Query<ProductReview> q = currentSession().createQuery(hql, ProductReview.class);
        q.setParameter("pid", productId);
        return q.getResultList();
    }

    @Override
    public long countByProductId(int productId) {
        // Đếm tổng số review của productId
        String hql = "SELECT count(pr) FROM ProductReview pr " +
                "WHERE pr.product.product_id = :pid";
        TypedQuery<Long> tq = currentSession().createQuery(hql, Long.class);
        tq.setParameter("pid", productId);
        return tq.getSingleResult();    }

    @Override
    public void save(ProductReview review) {
        currentSession().saveOrUpdate(review);
    }

    @Override
    public void delete(int reviewId) {
        // Lấy review theo id, nếu tồn tại thì xóa
        ProductReview pr = currentSession().get(ProductReview.class, reviewId);
        if (pr != null) {
            currentSession().delete(pr);
        }
    }

    @Override
    public ProductReview findByUserAndProduct(int userId, int productId) {
        String hql = "FROM ProductReview pr " +
                "LEFT JOIN FETCH pr.user u " +
                "WHERE pr.user.id = :uid AND pr.product.product_id = :pid";
        TypedQuery<ProductReview> query = currentSession().createQuery(hql, ProductReview.class);
        query.setParameter("uid", userId);
        query.setParameter("pid", productId);

        List<ProductReview> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
