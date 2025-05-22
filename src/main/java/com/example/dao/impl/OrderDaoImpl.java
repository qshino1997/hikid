package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.OrderDao;
import com.example.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl extends BaseDAO implements OrderDao {
    @Override
    public void save(Order order) {
        currentSession().saveOrUpdate(order);
    }

    @Override
    public Order findByPaymentId(String paymentId) {
        String hql = "FROM Order o WHERE o.payment_id = :pid";
        return currentSession()
                .createQuery(hql, Order.class)
                .setParameter("pid", paymentId)
                .uniqueResult();
    }
}
