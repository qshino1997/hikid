package com.example.service.Cart;

import com.example.dto.CartItemDto;
import com.example.dto.ProductDto;
import com.example.entity.CartItem;

import java.util.Collection;

public interface CartService {
    void addItem(ProductDto p, int qty);
    void updateItem(int productId, int qty);
    void removeItem(int productId);
    Collection<CartItemDto> getItems();
    int getTotalQuantity();
    long getTotal();
    boolean isEmpty();
    void clear();
    void initForUser(Integer userId);
}
