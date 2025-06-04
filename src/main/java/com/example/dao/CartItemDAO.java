package com.example.dao;

import com.example.entity.Cart;
import com.example.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemDAO {
    Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);
    List<CartItem> findByCartId(Integer cartId);
    void saveOrUpdate(CartItem item);
    void delete(CartItem item);
    void deleteAllByCart(Cart cart);
}
