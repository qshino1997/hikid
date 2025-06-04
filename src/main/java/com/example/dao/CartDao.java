package com.example.dao;

import com.example.entity.Cart;

public interface CartDao {
    Cart findByUserId(Integer userId);
    void saveOrUpdate(Cart cart);
}
