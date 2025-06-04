package com.example.dao.WebhookDialogFlowDAO.impl;

import com.example.dao.BaseDAO;
import com.example.dao.WebhookDialogFlowDAO.ProductWebhookDAO;
import com.example.dto.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductWebhookDAOImpl extends BaseDAO implements ProductWebhookDAO {
    @Override
    public List<ProductDto> findManufacturerName(String brand) {
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
                "WHERE lower(p.manufacturer.name) LIKE :kw ";

        return currentSession()
                .createQuery(hql, ProductDto.class)
                .setParameter("kw", "%" + brand + "%")
                .getResultList();
    }

    @Override
    public ProductDto findByName(String name){
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
                "WHERE p.name LIKE :kw ";

        return currentSession()
                .createQuery(hql, ProductDto.class)
                .setParameter("kw", "%" + name + "%")
                .uniqueResult();
    }
}
