package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.CartItemDAO;
import com.example.entity.Cart;
import com.example.entity.CartItem;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartItemDAOImpl extends BaseDAO implements CartItemDAO {

    @Override
    public Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId) {
        String hql = "FROM CartItem ci WHERE ci.cart.cart_id = :cartId AND ci.product.product_id = :productId";
        CartItem item = currentSession()
                .createQuery(hql, CartItem.class)
                .setParameter("cartId", cartId)
                .setParameter("productId", productId)
                .uniqueResult();
        return Optional.ofNullable(item);
    }

    @Override
    public List<CartItem> findByCartId(Integer cartId) {
        String hql = "FROM CartItem ci WHERE ci.cart.cart_id = :cartId";
        Query<CartItem> q = currentSession()
                .createQuery(hql, CartItem.class)
                .setParameter("cartId", cartId);
        return q.list();
    }

    @Override
    public void saveOrUpdate(CartItem item) {
        currentSession().saveOrUpdate(item);
    }

    @Override
    public void delete(CartItem item) {
        currentSession().delete(item);
    }

    @Override
    public void deleteAllByCart(Cart cart) {
        String hql = "DELETE FROM CartItem ci WHERE ci.cart.cart_id = :cartId";
        currentSession()
                .createQuery(hql)
                .setParameter("cartId", cart.getCart_id())
                .executeUpdate();
    }
}
