package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.RoleDAO;
import com.example.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl extends BaseDAO implements RoleDAO {
    @Override
    public Role findById(int id) {
        String hql = "From Role r" +
                " WHERE r.id = :id";
        return currentSession().createQuery(hql, Role.class).setParameter("id", id).uniqueResult();
    }
}
