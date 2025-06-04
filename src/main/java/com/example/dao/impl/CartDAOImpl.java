package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.CartDao;
import com.example.entity.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartDAOImpl extends BaseDAO implements CartDao {

    @Override
    public Cart findByUserId(Integer userId) {
        String hql = "SELECT distinct c FROM Cart c " +
                "LEFT JOIN FETCH c.items ci " +
                "LEFT JOIN FETCH ci.product p " +
                "LEFT JOIN FETCH p.image img " +
                " WHERE c.user.id = :userId";
        return currentSession().createQuery(hql, Cart.class).setParameter("userId", userId).uniqueResult();
    }

    @Override
    public void saveOrUpdate(Cart cart) {
        currentSession().saveOrUpdate(cart);
    }
}
