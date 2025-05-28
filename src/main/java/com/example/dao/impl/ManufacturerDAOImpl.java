package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.ManufacturerDAO;
import com.example.entity.Manufacturer;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ManufacturerDAOImpl extends BaseDAO implements ManufacturerDAO {
    @Override
    public List<Manufacturer> getAll() {
        return currentSession().createQuery("from Manufacturer", Manufacturer.class).getResultList();
    }

    @Override
    public Manufacturer findById(int id) {
        return currentSession().get(Manufacturer.class, id);
    }

    @Override
    public Manufacturer findByName(String name) {
        String hql = "FROM Manufacturer m WHERE m.name = :name";
        return currentSession().createQuery(hql, Manufacturer.class).setParameter("name", name).uniqueResult();
    }

    @Override
    public List<Manufacturer> findAll(int page, int size, String keyword) {
        String hql = "FROM Manufacturer m WHERE 1 = 1";
        if (keyword != null && !keyword.trim().isEmpty()) {
            hql += " AND m.name LIKE :kw";
        }
        hql += " ORDER BY m.id DESC";
        TypedQuery<Manufacturer> query = currentSession().createQuery(hql, Manufacturer.class);
        if (keyword != null && !keyword.trim().isEmpty()) {
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        }
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public long count(String keyword) {
        String hql = "SELECT COUNT(m) FROM Manufacturer m WHERE 1 = 1";
        if (keyword != null && !keyword.trim().isEmpty()) {
            hql += " AND LOWER(m.name) LIKE :kw";
        }
        TypedQuery<Long> query = currentSession().createQuery(hql, Long.class);
        if (keyword != null && !keyword.trim().isEmpty()) {
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        }
        return query.getSingleResult();
    }

    @Override
    public void saveOrUpdate(Manufacturer manufacturer) {
        currentSession().saveOrUpdate(manufacturer);
    }

    @Override
    public void deleteById(Integer id) {
        Manufacturer m = findById(id);
        if (m != null) {
            currentSession().delete(m);
        }
    }
}
