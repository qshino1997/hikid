package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.ProductDAO;
import com.example.dto.ProductDto;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class ProductDAOImpl extends BaseDAO implements ProductDAO {
    @Override
    public List<ProductDto> findAllByCatoryId(int id) {
        String hql = "select new com.example.dto.ProductDto(p.product_id, p.name, p.product_weight, p.price, p.made_in, p.appropriate_age_start, " +
                "p .appropriate_age_end, p.warning, p.instructions, p.storage_instructions, p.stock, " +
                "p.manufacturer.id, p.manufacturer.name, p.manufacturer.country) " +
                "from Product p where p.category.id = :id";

        return currentSession().createQuery(hql, ProductDto.class ).setParameter("id", id).getResultList();
    }

    @Override
    public ProductDto findById(int id) {
        String hql = "select new com.example.dto.ProductDto(p.product_id, p.name, p.product_weight, p.price, p.made_in, p.appropriate_age_start, " +
                "p .appropriate_age_end, p.warning, p.instructions, p.storage_instructions, p.stock, " +
                "p.manufacturer.id, p.manufacturer.name, p.manufacturer.country)" +
                "from Product p where p.product_id = :id";

        return currentSession().createQuery(hql, ProductDto.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public Integer findPriceByBrandAndAgeRange(String brand, String ageRange) {
        // Giải mã ageRange thành start và end (ví dụ "0-6 tháng")
        String[] parts = ageRange.split("[^0-9]+"); // tách chỉ số số
        int start = Integer.parseInt(parts[0]);
        int end = Integer.parseInt(parts[1]);

        String hql = "select p.price "
                + "from Product p "
                + "where p.manufacturer.name = :brand "
                + "  and p.appropriate_age_start = :start "
                + "  and p.appropriate_age_end = :end";
        return currentSession()
                .createQuery(hql, Integer .class)
                .setParameter("brand", brand)
                .setParameter("start", start)
                .setParameter("end", end)
                .uniqueResult();
    }
}
