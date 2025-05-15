package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.UserDAO;
import com.example.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Override
    public User findByEmail(String email) {
        String hql = "FROM User WHERE email = :email";
        return currentSession().createQuery(hql,User.class)
                .setParameter("email", email)
                .uniqueResult();
    }
}
