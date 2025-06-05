package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.ProductDAO;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDAOImpl extends BaseDAO implements ProductDAO {
    @Override
    public List<ProductDto> findAllByCatoryId(int categoryId, int page, int size) {
        // 1) Build HQL với 2 fetch-join
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT new com.example.dto.ProductDto(" +
                        "p.product_id, " +
                        "p.name, " +
                        "p.product_weight, " +
                        "p.price, " +
                        "p.made_in, " +
                        "p.appropriate_age_start, " +
                        "p.appropriate_age_end, " +
                        "p.warning, " +
                        "p.instructions, " +
                        "p.storage_instructions, " +
                        "p.stock, " +
                        "p.category.id, " +
                        "p.category.name, " +
                        "p.manufacturer.id, " +
                        "p.manufacturer.name, " +
                        "i.url) ")
                .append("FROM Product p ")
                .append("LEFT JOIN p.image i ")
                .append("WHERE p.category.id = :id ");

        Query<ProductDto> contentQuery = currentSession().createQuery(hql.toString(), ProductDto.class);
        contentQuery.setParameter("id", categoryId);
        contentQuery.setFirstResult((page - 1) * size);
        contentQuery.setMaxResults(size);
        return contentQuery.getResultList();
    }

    @Override
    public long countByCategoryId(int categoryId){
        TypedQuery<Long> q = currentSession().createQuery("SELECT count(p) FROM Product p WHERE p.category.id = :cid", Long.class);
        q.setParameter("cid", categoryId);
        return q.getSingleResult();
    }

    @Override
    public Product findById(int id) {
        return currentSession().createQuery("FROM Product p WHERE p.product_id = :id", Product.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public Product findByName(String name){
        return currentSession().createQuery("FROM Product p WHERE p.name = :name", Product.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public List<ProductDto> findByKeyword(int page, int size, String keyword) {
        // 1) Build HQL với 2 fetch-join
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT new com.example.dto.ProductDto(" +
                        "p.product_id, " +
                        "p.name, " +
                        "p.product_weight, " +
                        "p.price, " +
                        "p.made_in, " +
                        "p.appropriate_age_start, " +
                        "p.appropriate_age_end, " +
                        "p.warning, " +
                        "p.instructions, " +
                        "p.storage_instructions, " +
                        "p.stock, " +
                        "p.category.id, " +
                        "p.category.name, " +
                        "p.manufacturer.id, " +
                        "p.manufacturer.name, " +
                        "i.url) ")
                .append("FROM Product p ")
                .append("LEFT JOIN p.image i ")
                .append("WHERE 1 = 1 ");

        // 2) Thêm điều kiện lọc theo tên nếu có keyword
        if (StringUtils.hasText(keyword)) {
            hql.append("AND p.name LIKE :kw ");
        }

        // 3) Tạo query
        Query<ProductDto> q = currentSession().createQuery(hql.toString(), ProductDto.class);

        if (StringUtils.hasText(keyword)) {
            q.setParameter("kw", "%" + keyword.trim() + "%");
        }

        // 4) Phân trang (ManyToOne fetch-join không ảnh hưởng paging)
        q.setFirstResult((page - 1) * size);
        q.setMaxResults(size);

        return q.getResultList();
    }

    @Override
    public long countByKeyword(String keyword) {
        StringBuilder hql = new StringBuilder("SELECT count(p) FROM Product p WHERE 1=1");
        if (StringUtils.hasText(keyword)) {
            hql.append(" AND p.name LIKE :kw");
        }
        TypedQuery<Long> q = currentSession().createQuery(hql.toString(), Long.class);
        if (StringUtils.hasText(keyword)) {
            q.setParameter("kw", "%" + keyword.trim() + "%");
        }
        return q.getSingleResult();
    }

    @Override
    public List<ProductDto> findByCategory0AndKeyword(int page, int size, String keyword) {
        // 1) Build HQL với 2 fetch-join
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT new com.example.dto.ProductDto(" +
                        "p.product_id, " +
                        "p.name, " +
                        "p.product_weight, " +
                        "p.price, " +
                        "p.made_in, " +
                        "p.appropriate_age_start, " +
                        "p.appropriate_age_end, " +
                        "p.warning, " +
                        "p.instructions, " +
                        "p.storage_instructions, " +
                        "p.stock, " +
                        "p.category.id, " +
                        "p.category.name, " +
                        "p.manufacturer.id, " +
                        "p.manufacturer.name, " +
                        "i.url) ")
                .append("FROM Product p ")
                .append("LEFT JOIN p.image i ")
                .append("WHERE 1 = 1 ");

        if (keyword != null && !keyword.isBlank()) {
            hql.append("AND (lower(p.name) LIKE :kw OR lower(p.manufacturer.name) LIKE :kw) ");
        }
        Query<ProductDto> contentQuery = currentSession().createQuery(hql.toString(), ProductDto.class);
        if (keyword != null && !keyword.isBlank()) {
            contentQuery.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        }
        contentQuery.setFirstResult((page - 1) * size);
        contentQuery.setMaxResults(size);
        return  contentQuery.getResultList();
    }

    @Override
    public long countByCategory0AndKeyword(String keyword) {
        String hql = "SELECT count(p) FROM Product p "
                + "JOIN p.manufacturer m "
                + "WHERE 1 = 1 ";

        if (keyword != null && !keyword.isBlank()) {
            hql += "AND (lower(p.name) LIKE :kw OR lower(m.name) LIKE :kw) ";
        }

        Query<Long> q = currentSession().createQuery(hql, Long.class);

        if (keyword != null && !keyword.isBlank()) {
            q.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        }
        return q.uniqueResult();
    }

    @Override
    public List<ProductDto> findByCategoryId(int categoryId) {
        String hql = "SELECT new com.example.dto.ProductDto(" +
                "p.product_id, " +
                "p.name, " +
                "p.product_weight, " +
                "p.price, " +
                "p.made_in, " +
                "p.appropriate_age_start, " +
                "p.appropriate_age_end, " +
                "p.warning, " +
                "p.instructions, " +
                "p.storage_instructions, " +
                "p.stock, " +
                "p.category.id, " +
                "p.category.name, " +
                "p.manufacturer.id, " +
                "p.manufacturer.name, " +
                "i.url) " +
                "FROM Product p " +
                "LEFT JOIN p.image i " +
                "WHERE p.category.id = :categoryId ";
        return currentSession().createQuery(hql, ProductDto.class).setParameter("categoryId", categoryId).getResultList();
    }

    @Override
    public void save(Product product) {
        currentSession().saveOrUpdate(product);
    }

    @Override
    public void delete(int id) {
        Product product = currentSession().get(Product.class, id);
        if(product != null){
            currentSession().delete(product);
        }
    }

    @Override
    public ProductDto getById(int id) {
        String hql = "SELECT new com.example.dto.ProductDto(" +
                "p.product_id, " +
                "p.name, " +
                "p.product_weight, " +
                "p.price, " +
                "p.made_in, " +
                "p.appropriate_age_start, " +
                "p.appropriate_age_end, " +
                "p.warning, " +
                "p.instructions, " +
                "p.storage_instructions, " +
                "p.stock, " +
                "p.category.id, " +
                "p.category.name, " +
                "p.manufacturer.id, " +
                "p.manufacturer.name, " +
                "i.url) " +
                "FROM Product p " +
                "LEFT JOIN p.image i " +
                "WHERE p.product_id = :id ";
    return currentSession().createQuery(hql, ProductDto.class).setParameter("id", id).uniqueResult();
    }

    @Override
    public List<ProductDto> getMaxProducesBySix(int categoryId) {
        // 1) Build HQL với 2 fetch-join
        String hql = "SELECT new com.example.dto.ProductDto(" +
                "p.product_id, " +
                "p.name, " +
                "p.product_weight, " +
                "p.price, " +
                "p.made_in, " +
                "p.appropriate_age_start, " +
                "p.appropriate_age_end, " +
                "p.warning, " +
                "p.instructions, " +
                "p.storage_instructions, " +
                "p.stock, " +
                "p.category.id, " +
                "p.category.name, " +
                "p.manufacturer.id, " +
                "p.manufacturer.name, " +
                "i.url) " +
                "FROM Product p " +
                "LEFT JOIN p.image i " +
                "WHERE p.category.id = :id " +
                "ORDER BY p.price DESC";

        return currentSession()
                .createQuery(hql, ProductDto.class)
                .setParameter("id", categoryId)
                .setMaxResults(6)
                .getResultList();
    }
}
