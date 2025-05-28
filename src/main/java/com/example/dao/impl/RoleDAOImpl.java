package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.RoleDAO;
import com.example.entity.Role;

public class RoleDAOImpl extends BaseDAO implements RoleDAO {
    @Override
    public Role findById(int id) {
        return currentSession().get(Role.class, id);
    }
}
