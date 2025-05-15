package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.CategoryDAO;
import com.example.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDAOImpl extends BaseDAO implements CategoryDAO  {

    @Override
    public Category findById(int id) {
        String hql = "From Category c where c.id = :id";
        return currentSession().createQuery(hql, Category.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Category> findAllWithSubcategories() {
        // Lấy tất cả category gốc (parent = null) kèm subCategories
        String hql =
                "SELECT DISTINCT c " +
                        "FROM Category c " +
                        " LEFT JOIN FETCH c.subCategories sc " +
                        " LEFT JOIN FETCH sc.subCategories " +
                        "WHERE c.parent IS NULL";
        return currentSession().createQuery(hql, Category.class).getResultList();
    }

    @Override
    public List<Category> findByIdWithSubcategories(Integer parentId) {
        String hql = "FROM Category c WHERE c.parent.id = :id";
        return currentSession()
                .createQuery(hql, Category.class)
                .setParameter("id", parentId)
                .getResultList();
    }
}
