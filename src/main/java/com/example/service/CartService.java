package com.example.service;

import com.example.dto.CartItem;
import com.example.dto.ProductDto;

import java.util.Collection;

public interface CartService {
    void addItem(ProductDto p, int qty);
    void updateItem(int productId, int qty);
    void removeItem(int productId);
    Collection<CartItem> getItems();
    int getTotalQuantity();
    long getTotal();
    boolean isEmpty();
    void clear();
}
