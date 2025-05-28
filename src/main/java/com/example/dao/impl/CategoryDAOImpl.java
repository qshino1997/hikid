package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.CategoryDAO;
import com.example.entity.Category;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class CategoryDAOImpl extends BaseDAO implements CategoryDAO  {

    @Override
    public Category findById(int id) {
        String hql = "From Category c" +
                        " LEFT JOIN FETCH c.subCategories sc" +
                        " LEFT JOIN FETCH sc.subCategories" +
                        " LEFT JOIN FETCH c.parent" +
                        " LEFT JOIN FETCH c.products  " +
                        " WHERE c.id = :id";
        return currentSession().createQuery(hql, Category.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Category> findAll() {
        // Lấy tất cả category gốc (parent = null) kèm subCategories
        String hql =
                "SELECT DISTINCT c " +
                        "FROM Category c " +
                        " LEFT JOIN FETCH c.subCategories sc" +
                        " LEFT JOIN FETCH sc.subCategories ";
        return currentSession().createQuery(hql, Category.class).getResultList();
    }

    @Override
    public List<Category> findAllWithSubcategories() {
        // Lấy tất cả category gốc (parent = null) kèm subCategories
        String hql =
                "SELECT DISTINCT c " +
                        "FROM Category c " +
                        " LEFT JOIN FETCH c.subCategories sc " +
                        " LEFT JOIN FETCH sc.subCategories scc " +
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

    @Override
    public List<Category> findByKeyword(int page, int size, String keyword) {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c FROM Category c ")
                .append("LEFT JOIN FETCH c.parent p ")       // fetch parent
                .append("WHERE 1=1 ");
        if (StringUtils.hasText(keyword)) {
            hql.append("AND c.name LIKE :kw ");
        }

        Query<Category> q = currentSession().createQuery(hql.toString(), Category.class);
        if (StringUtils.hasText(keyword)) {
            q.setParameter("kw", "%" + keyword.trim() + "%");
        }
        q.setFirstResult((page - 1) * size);
        q.setMaxResults(size);
        return q.getResultList();
    }

    @Override
    public long countByKeyword(String keyword) {
        StringBuilder hql = new StringBuilder("SELECT count(c) FROM Category c WHERE 1=1 ");
        if (StringUtils.hasText(keyword)) {
            hql.append("AND c.name LIKE :kw");
        }
        Query<Long> q = currentSession().createQuery(hql.toString(), Long.class);
        if (StringUtils.hasText(keyword)) {
            q.setParameter("kw", "%" + keyword.trim() + "%");
        }
        return q.uniqueResult();
    }

    @Override
    public void saveOrUpdate(Category category) {
        currentSession().saveOrUpdate(category);
    }

    @Override
    public void deleteById(Integer id) {
        // 1. Load entity ra
        Category category = currentSession().get(Category.class, id);
        // 2. Nếu tồn tại thì xoá
        if (category != null) {
            currentSession().delete(category);
        }
    }

    @Override
    public Category findByName(String name) {
        String hql = "FROM Category c WHERE c.name = :name";

        return currentSession().createQuery(hql, Category.class).setParameter("name", name).uniqueResult();
    }
}
