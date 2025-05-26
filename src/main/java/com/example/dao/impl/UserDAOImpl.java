package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.UserDAO;
import com.example.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Override
    public User findByIdAndProfile(int id) {
        String hql = "SELECT u FROM User u " +
                "LEFT JOIN FETCH u.profile WHERE u.id = :id";
        return currentSession().createQuery(hql,User.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public User findByEmail(String email) {
        String hql = "SELECT u FROM User u " +
                "LEFT JOIN FETCH u.profile WHERE u.email = :email";
        return currentSession().createQuery(hql,User.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    @Override
    public void saveOrUpdate(User user) {
        currentSession().saveOrUpdate(user);
    }

    @Override
    public List<User> getAllEmployees() {
        String hql = "SELECT u FROM User u " +
                "LEFT JOIN FETCH u.profile " +
                "LEFT JOIN FETCH u.roles r " +
                "WHERE r.id = 2";
        return currentSession().createQuery(hql,User.class).getResultList();
    }

    @Override
    public List<User> getAllCustomers() {
        String hql = "SELECT u FROM User u " +
                "LEFT JOIN FETCH u.profile " +
                "LEFT JOIN FETCH u.roles r " +
                "WHERE r.id = 3";
        return currentSession().createQuery(hql,User.class).getResultList();
    }

    @Override
    public User findById(int id) {
        return currentSession().get(User.class, id);
    }

    @Override
    public void deleteById(int id) {
        // 1. Load entity ra
        User user = currentSession().get(User.class, id);
        // 2. Nếu tồn tại thì xoá
        if (user != null) {
            currentSession().delete(user);
        }
    }

}
