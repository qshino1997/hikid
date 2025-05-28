package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.UserDAO;
import com.example.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
    public List<User> getAllUsers(int page, int size, int role, String keyword) {
        String hql = "SELECT u FROM User u " +
                "LEFT JOIN FETCH u.profile p " +
                "LEFT JOIN FETCH u.roles r " +
                "WHERE r.id = :role";

        if (keyword != null && !keyword.trim().isEmpty()) {
            hql += " AND u.username LIKE :kw";
        }

        Query<User> q = currentSession().createQuery(hql, User.class);
        q.setParameter("role", role);
        if (keyword != null && !keyword.isEmpty()) {
            q.setParameter("kw", "%" + keyword.trim() + "%");
        }

        q.setFirstResult((page - 1) * size);
        q.setMaxResults(size);

        return q.getResultList();
    }

    @Override
    public long countEmployees(int role, String keyword) {
        String hql = "SELECT count(u) FROM User u JOIN u.roles r WHERE r.id = :role";

        if (keyword != null && !keyword.trim().isEmpty()) {
            hql += " AND u.username LIKE :kw";
        }

        Query<Long> q = currentSession().createQuery(hql, Long.class);
        q.setParameter("role", role);

        if (keyword != null && !keyword.isEmpty()) {
            q.setParameter("kw", "%" + keyword.trim() + "%");
        }

        return q.uniqueResult();
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
            user.getRoles().clear();
            currentSession().delete(user);
        }
    }

}
